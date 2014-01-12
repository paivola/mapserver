package fi.paivola.mapserver.core;

import fi.paivola.mapserver.core.setting.Setting;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.CCs;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.json.simple.JSONObject;
import org.json.simple.parser.ParseException;

/**
 * GameManager handles the simulation.
 *
 * @author Juhani
 */
public class GameManager {

    /**
     * How many ticks are there in the whole simulation.
     */
    public int tick_amount;
    /**
     * Current tick.
     */
    public int tick_current;
    /**
     * Models that are available.
     */
    private Map<String, CCs> models;
    /**
     * All your dataframe are belong to us.
     */
    private final List<DataFrame> frames;
    /**
     * All of the active models, EG. objects.
     */
    private final Map<String, Model> active_models;
    private SettingsParser sp;
    /**
     * How many models are active / where are we going.
     */
    public int current_id;
    /**
     * Should we print final data when done?
     */
    public int printOnDone = 0;
    
    private final static Logger log = Logger.getLogger("mapserver");
    
    /**
     * Are we done?
     */
    boolean ready = false;

    public GameManager(int tick_amount, InputStream settings_file) {
        this.tick_amount = tick_amount;
        this.tick_current = 0;
        this.frames = new ArrayList<>();
        this.active_models = new HashMap<>();
        this.current_id = 0;
        log.setLevel(Level.FINE);

        try {
            this.sp = new SettingsParser(settings_file);
            this.models = this.sp.getModels();
        } catch (IOException | ParseException ex) {
            Logger.getLogger(GameManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }

        DataFrame.dataSeperator = SettingsParser.settings.get("csv_seperator").toString();
        clearFrames();
        runRegisterations();
    }

    public GameManager(int tick_amount) {
        this(tick_amount, null);
    }

    /**
     * Runs all of the onRegisteration functions in models.
     */
    private void runRegisterations() {
        log.log(Level.FINE, "Registering models ({0})...", this.models.size());
        for (Map.Entry pair : this.models.entrySet()) {
            Class cls;
            cls = (Class) ((CCs) pair.getValue()).cls;
            Constructor<Model> c;
            log.log(Level.FINE, "Registering {0}", pair.getKey());
            
            SettingMaster blank = new SettingMaster();
            
            try {
                c = cls.getDeclaredConstructor();
                c.setAccessible(true);
                Model m;
                try {
                    m = c.newInstance();
                    m.onActualRegisteration(this, blank);
                    ((CCs) pair.getValue()).sm = blank;
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
     * Get all of the default SettingMasters.
     * 
     * @return 
     */
    public Map<String, JSONObject> getSettings() {
        Map<String, JSONObject> settings = new HashMap<>();
        for (Map.Entry pair : this.models.entrySet()) {
            settings.put(pair.getKey().toString(), ((CCs)pair.getValue()).sm.getJSONObject());
        }
        return settings;
    }
    
    /**
     * Get all of the data formatted somehow.
     * 
     * @return 
     */
    public Map<String, String[]> getData() {
        Map<String, String[]> data = new HashMap<>();
        
        for (int i = 0; i < this.tick_amount; i++) {
            data.put(""+i, this.frames.get(i).getATonOfStrings());
        }
        
        return data;
    }

    /**
     * Initializes all of the dataframes.
     */
    private void clearFrames() {
        frames.clear();
        for (int i = 0; i <= this.tick_amount; i++) {
            frames.add(new DataFrame(i));
        }
    }

    public boolean addModel(Model m, String type) {
        m.addExtensions(this, models.get(type).clss);
        return (this.active_models.put(""+m.id, m) == null);
    }

    /**
     * Creates a model based on a SettingMaster.
     *
     * @param type name of the models type
     * @return returns the model if success, null otherwise
     */
    public Model createModel(String type, SettingMaster sm) {
        Class cls;
        cls = (Class) models.get(type).cls;
        Model m = null;
        try {
            Constructor<Model> c;
            c = cls.getDeclaredConstructor(int.class, SettingMaster.class);
            c.setAccessible(true);
            try {
                m = c.newInstance(this.current_id++, sm);
            } catch (InstantiationException | IllegalAccessException |
                    IllegalArgumentException | InvocationTargetException ex) {
                Logger.getLogger(GameManager.class.getName())
                        .log(Level.SEVERE, null, ex);
            }
        } catch (NoSuchMethodException | SecurityException ex) {
            Logger.getLogger(GameManager.class.getName())
                    .log(Level.SEVERE, null, ex);
        }
        return m;
    }
    
    /**
     * Creates a model with the default SettingMaster for that model.
     * @param type name of the models type
     * @return returns the model if success, null otherwise
     */
    public Model createModel(String type) {
        return this.createModel(type, this.models.get(type).sm);
    }

    /**
     * Links two models together.
     *
     * @param first first model
     * @param second second model
     * @return returns true if successful, false otherwise
     */
    public boolean linkModels(Model first, Model second) {
        if (!first.linkModel(second)) {
            return false;
        }
        if (!second.linkModel(first)) {
            first.unlinkModel(second);
            return false;
        }

        return true;
    }
    
    /**
     * Do we know a model by this id. ToDo: Better way :-)
     * 
     * @param id id of the model
     * @return   Returns true if contains, false otherwise
     */
    public boolean containsModel(int id) {
        return id<this.current_id;
    }
    
    /**
     * Gets a model by id.
     * 
     * @param id id of the model
     * @return   Returns the model
     */
    public Model getActive(String id) {
        return this.active_models.get(id);
    }
    
    /**
     * Gets the default SettingMaster for specified model type.
     * 
     * @param type what model type are you looking for
     * @return SettingMaster
     */
    public SettingMaster getDefaultSM(String type) {
        return this.models.get(type).sm;
    }
    
    /**
     * Gets the default SettingMaster for specified class.
     * 
     * @param cls what model type are you looking for
     * @return SettingMaster
     */
    public SettingMaster getDefaultSM(Class cls) {
        /**
         * Lets find our name...
         */
        String type = "";
        for(Entry<String, CCs> e : this.models.entrySet()) {
            if(cls.equals(e.getValue().cls)) {
                type = e.getKey();
            }
        }
        return this.models.get(type).sm;
    }

    /**
     * Supposed to populate the first dataframe with default values.
     *
     * @param m model from where to get the defaults
     * @param df dataframe to populate to
     * @return returns true
     */
    public boolean populateDefaults(Model m, DataFrame df) {

        m.onGenerateDefaults(df);
        m.dumpToDataFrame(df);

        return true;
    }

    /**
     * Steps trough all of the frames.
     *
     * @return returns true
     */
    public boolean stepTrough() {
        
        log.log(Level.FINE, "Stepping trough");

        while (this.tick_current <= this.tick_amount) {
            this.step();
            if(this.printOnDone == 2) {
                String[] tmparr = this.frames.get(this.tick_current - 1).getATonOfStrings();
                System.out.println("TICK: "+(this.tick_current - 1));
                for (String s : tmparr) {
                    System.out.println(s);
                }
            }
        }
        
        this.ready = true;
        log.log(Level.FINE, "Stepped trough");
        
        if(this.printOnDone == 1) {
            for(Entry<String, String[]> e : this.getData().entrySet()) {
                System.out.println("TICK: "+e.getKey());
                for(String s : e.getValue()) {
                    System.out.println(s);
                }
            }
        }

        return true;
    }

    /**
     * Steps trough one frame. If it's the first one, we populate the defaults.
     *
     * @return true
     */
    public boolean step() {

        log.log(Level.FINE, "Running step {0}", this.tick_current);

        DataFrame current = this.frames.get(this.tick_current);
        current.locked = false;
        if (this.tick_current > 0) {
            DataFrame last = this.frames.get(this.tick_current - 1);
            for (Model value : this.active_models.values()) {
                value.onTickStart(last, current);
            }
        } else { //step 0 needs to use default values
            log.log(Level.FINE, "Generating defaults ({0})", this.active_models.size());
            for (Model value : this.active_models.values()) {
                this.populateDefaults(value, current);
            }
            log.log(Level.FINE, "Done");
        }
        current.locked = true;

        this.tick_current++;

        return true;
    }

    /**
     * Adds a extender that will be added to each subsequent target model.
     *
     * @param towhere the models name where to add
     * @param name name of the extender
     * @param cls the extending class
     */
    public void registerExtension(String towhere, String name, Object cls) {
        this.models.get(towhere).clss.put(name, cls);
    }

}
