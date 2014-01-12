package fi.paivola.mapserver.core;

import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.LatLng;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * The base class for all models. Has some basic functions that you will surely
 * find useful.
 *
 * @author Juhani
 */
public abstract class Model {

    /**
     * Each model has a ID. It's used for identification and iteration.
     */
    public int id;
    /**
     * Type of the model (point, connection...)
     */
    public String type;
    /**
     * List of all connections (connection models).
     */
    public List<Model> connections;
    /**
     * List of events waiting.
     */
    public List<Event> events;
    /**
     * All data that are automatically saved to a DataFrame.
     */
    public Map<String, Object> data;
    /**
     * Extension models that are active.
     */
    public Map<String, ExtensionModel> extensions;
    /**
     * Maximum connections.
     */
    public int maxConnections;
    /**
     * Latitude & Longitude
     */
    public LatLng ll;
    /**
     * List of allowed connection names.
     */
    public List<String> allowedNames;
    /**
     * Name of this model.
     */
    public String name;

    /**
     * Is this model a prototype model or not?
     */
    public boolean proto;
    /**
     * Is this model in the need for a SM?
     */
    public boolean needsSM;

    /**
     *
     *
     * @param id id of this model
     */
    public Model(int id) {
        this.id = id;
        this.connections = new ArrayList<>();
        this.events = new ArrayList<>();
        this.data = new HashMap();
        this.extensions = new HashMap();
        this.allowedNames = new ArrayList<>();
        this.proto = id == -1;
        this.needsSM = !this.proto;
        this.ll = new LatLng(0, 0);
        this.name = "";
    }

    /**
     * Change the latitude and longitude
     *
     * @param lat latitude
     * @param lng longitude
     */
    public void setLatLng(double lat, double lng) {
        this.ll.latitude = lat;
        this.ll.longitude = lng;
    }

    /**
     * Change the latitude and longitude
     *
     * @param ll LatLng that is the desired position
     */
    public void setLatLng(LatLng ll) {
        this.ll = ll;
    }

    /**
     * Get the latitude and longitude as a LatLng
     *
     * @return
     */
    public LatLng getLatLng() {
        return this.ll;
    }

    /**
     * How far is this model from another one.
     *
     * @param m The other model
     * @return distance in km.
     */
    public double distanceTo(Model m) {
        return this.ll.distanceTo(m.ll);
    }

    /**
     * Internal function that checks the events and runs extensions.
     *
     * @param last last dataframe
     * @param current current dataframe
     */
    public void onTickStart(DataFrame last, DataFrame current) {
        // breaking prototype models legs...
        if (this.proto || this.needsSM) {
            return;
        }

        // lets check if there is some events waiting to get trough
        // fixes java.util.ConcurrentModificationException
        List<Event> _buf = new ArrayList<>();
        for (Event i : this.events) {
            if (i.frame == last.index) {
                _buf.add(i);
            }
        }
        for (Event i : _buf) {
            this.onEvent(i, current);
        }

        this.onTick(last, current);
        for (Map.Entry pairs : this.extensions.entrySet()) {
            // lets go trough the events ONCE again... this time for extensions
            ((ExtensionModel) pairs.getValue()).data = data;
            for (Event i : _buf) {
                ((ExtensionModel) pairs.getValue()).onEvent(i, current);
            }
            ((ExtensionModel) pairs.getValue())
                    .onTickStart(last, current);
            data = ((ExtensionModel) pairs.getValue()).data;
        }

        // lets delete the events that we used
        Iterator<Event> it = this.events.iterator();
        while (it.hasNext()) {
            if (it.next().frame == last.index) {
                it.remove();
            }
        }

        this.dumpToDataFrame(current);
    }

    /**
     * Dumps all data in data map to a dataframe
     *
     * @param frame frame to dump to
     */
    public void dumpToDataFrame(DataFrame frame) {
        for (Map.Entry pairs : this.data.entrySet()) {
            frame.saveData(this,
                    pairs.getKey().toString(), pairs.getValue().toString());
        }
    }

    /**
     * Adds an event to this model
     *
     * @param e event to add
     * @param m model from where the event originates
     */
    public void addEvent(Event e, Model m) {
        this.events.add(e);
    }

    /**
     * Adds a event to all connections.
     *
     * @param model the target of the event
     * @param frame dataframe of when it's added
     * @param e event thats added
     */
    public void addEventTo(Model model, DataFrame frame, Event e) {
        e.frame = frame.index;
        e.sender = this;
        model.addEvent(e, this);
    }

    /**
     * Adds a event to all connections.
     *
     * @param frame dataframe of when it's added
     * @param e event thats added
     */
    public void addEventToAll(DataFrame frame, Event e) {
        e.frame = frame.index;
        e.sender = this;
        for (Model i : this.connections) {
            i.addEvent(e, this);
        }
    }

    /**
     * Adds a extension model.
     *
     * @param name name for the extension model to go by
     * @param extension the extension model
     * @return returns true if succeeded, false otherwise
     */
    public boolean addExtension(String name, ExtensionModel extension) {
        return this.extensions.put(name, extension) == null;
    }

    /**
     * Adds a bunch of extension models from a map of strings and classes. ToDo:
     * respect settings!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!
     *
     * @param gm game master
     * @param clss map of strings and classes
     */
    public void addExtensions(GameManager gm, Map<String, Object> clss) {
        for (Map.Entry pair : clss.entrySet()) {
            Class cls;
            cls = (Class) pair.getValue();
            Constructor<Model> c;
            try {
                c = cls.getDeclaredConstructor(int.class);
                c.setAccessible(true);
                try {
                    ExtensionModel em = (ExtensionModel) c.newInstance(gm.current_id++);
                    em.parent = this;
                    em.onActualUpdateSettings(gm.getDefaultSM((Class) pair.getValue()));
                    this.addExtension(pair.getKey().toString(), em);
                } catch (InstantiationException | IllegalAccessException | IllegalArgumentException | InvocationTargetException ex) {
                    Logger.getLogger(GameManager.class.getName())
                            .log(Level.SEVERE, null, ex);
                }
            } catch (NoSuchMethodException | SecurityException ex) {
                Logger.getLogger(GameManager.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        }
    }

    /**
     * Links this model to another model. Note: This is not two way. You need to
     * link it in the other way also.
     *
     * @param m what to link to
     * @return true if succeeded, false otherwise
     */
    public boolean linkModel(Model m) {
        if (this.connections.size() >= this.maxConnections) {
            return false;
        }
        if (this.allowedNames.isEmpty() || this.allowedNames.contains(m.name)) {
            return this.connections.add(m);
        }
        return false;
    }

    /**
     * Removes a link to another model. Note: this is not two way. You need to
     * unlink it in the other way also.
     *
     * @param m what to unlink from
     * @return True if succeeded, false otherwise
     */
    public boolean unlinkModel(Model m) {
        return this.connections.remove(m);
    }

    /**
     * Saves an integer. Will be saved to dataframe.
     *
     * @param name name of the integer
     * @param a actual integer
     * @return
     */
    public boolean saveInt(String name, int a) {
        return this.data.put(name, "" + a) == null;
    }

    /**
     * Saves a double. Will be saved to dataframe.
     *
     * @param name name of the double
     * @param a actual double
     * @return
     */
    public boolean saveDouble(String name, double a) {
        return this.data.put(name, "" + a) == null;
    }

    /**
     * Saves a string. Will be saved to dataframe.
     *
     * @param name name of the string
     * @param a actual string
     * @return
     */
    public boolean saveString(String name, String a) {
        return this.data.put(name, a) == null;
    }

    /**
     * Saves a object. Will be saved to dataframe.
     *
     * @param name
     * @param a
     * @return
     */
    public boolean saveData(String name, Object a) {
        return this.data.put(name, a) == null;
    }

    /**
     * Gets an integer.
     *
     * @param name name of the integer
     * @return the integer or null
     */
    public int getInt(String name) {
        return parseInt(getData(name).toString());
    }

    /**
     * Gets a double.
     *
     * @param name name of the double
     * @return the double or null
     */
    public double getDouble(String name) {
        return parseDouble(getData(name).toString());
    }

    /**
     * Gets a string.
     *
     * @param name name of the string
     * @return the string or null
     */
    public String getString(String name) {
        return getData(name).toString();
    }

    /**
     * Gets a object.
     *
     * @param name name of the object
     * @return the object or null
     */
    public Object getData(String name) {
        return this.data.get(name);
    }

    /**
     * Is called once the internal stuff (onTickStart) has finished, so it's
     * called once every tick.
     *
     * @param last last dataframe
     * @param current current dataframe
     */
    public abstract void onTick(DataFrame last, DataFrame current);

    /**
     * Called when there is a event to handle.
     *
     * @param e event thats handled
     * @param current current dataframe
     */
    public abstract void onEvent(Event e, DataFrame current);

    /**
     * This is called from the GameManager and performs some extra steps before
     * the users own onRegisteration is called.
     *
     * @param gm
     * @param sm
     */
    public void onActualRegisteration(GameManager gm, SettingMaster sm) {
        sm.type = this.type;
        this.onRegisteration(gm, sm);
        if (this.name.isEmpty()) {
            this.name = sm.name;
        } else if (sm.name.isEmpty()) {
            sm.name = this.name;
        }
        this.allowedNames = sm.allowedNames;

    }

    /**
     * Called when a particular model is added to the database of possible
     * models. Add settings here! Useful if you need to, say, register the
     * current model as a extension to some other model.
     *
     * @param gm game manager
     * @param sm setting master
     */
    public abstract void onRegisteration(GameManager gm, SettingMaster sm);

    /**
     * Called when there is a need for default values (for step 0). Calls
     * onGenerateDefaults on this and children.
     *
     * @param df
     */
    public void onActualGenerateDefaults(DataFrame df) {
        this.onGenerateDefaults(df);
        for (Map.Entry pairs : this.extensions.entrySet()) {
            ((ExtensionModel) pairs.getValue()).data = data;
            ((ExtensionModel) pairs.getValue()).onGenerateDefaults(df);
            data = ((ExtensionModel) pairs.getValue()).data;
        }
        this.dumpToDataFrame(df);
    }

    /**
     * Called when the module is asked for defaults, use save* here.
     *
     * @param df dataframe
     */
    public abstract void onGenerateDefaults(DataFrame df);

    /**
     * Called when there is a new SettingMaster and this model needs to be
     * updated.
     *
     * @param sm
     */
    public void onActualUpdateSettings(SettingMaster sm) {
        this.needsSM = false;
        if (this.name.isEmpty()) {
            this.name = sm.name;
        }
        this.onUpdateSettings(sm);

        for (Map.Entry pairs : this.extensions.entrySet()) {
            ((ExtensionModel) pairs.getValue()).onActualUpdateSettings(sm);
        }
    }

    /**
     * Called when there are settings to be changed, HANDLE THE CHANGES PLEASE.
     *
     * @param sm
     */
    public abstract void onUpdateSettings(SettingMaster sm);
}
