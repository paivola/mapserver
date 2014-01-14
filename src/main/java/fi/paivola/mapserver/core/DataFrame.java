package fi.paivola.mapserver.core;

import fi.paivola.mapserver.utils.StringPair;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * DataFrame is used for saving data permanently. Either for other models to get
 * it or for exporting to CSV.
 *
 * @author Juhani
 */
public class DataFrame {

    public int index;
    public boolean locked;
    private final ConcurrentHashMap<StringPair, Object> data;
    public static String dataSeperator = ", ";
    private final List<Event> events;

    /**
     * Class constructor.
     *
     * @param index the number of this frame
     */
    public DataFrame(int index) {
        this.index = index;
        this.data = new ConcurrentHashMap();
        this.events = new ArrayList<>();
        this.locked = true;
    }

    /**
     * Saves some data.
     *
     * This is to save data permanently, for other models to get it and for
     * later when it's time to export statistics.
     *
     * @param model model thats the source
     * @param name name of the field to save to
     * @param data value of the field as a string
     * @return true if succeeded, otherwise false
     */
    public boolean saveData(Model model, String name, String data) {
        if (this.locked) {
            return false;
        }
        return this.data.put(new StringPair(Integer.toString(model.id), name), data) == null;
    }

    /**
     * Saves a piece of global data.
     *
     * @param name name of the data
     * @param data data itself
     * @return returns true if succeeded
     */
    public boolean saveGlobalData(String name, Object data) {
        if (this.locked) {
            return false;
        }
        return this.data.put(new StringPair(" ", name), data) == null;
    }

    /**
     * Gets a global piece of data.
     *
     * @param name name of the data
     * @return returns the data
     */
    public Object getGlobalData(String name) {
        return this.data.get(new StringPair(" ", name));
    }

    /**
     * Gets a global string.
     *
     * @param name name of the data
     * @return returns the string
     */
    public String getGlobalString(String name) {
        return this.getGlobalData(name).toString();
    }

    /**
     * Gets a global integer.
     *
     * @param name name of the data
     * @return returns the integer
     */
    public int getGlobalInt(String name) {
        return Integer.parseInt(this.getGlobalData(name).toString());
    }

    /**
     * Gets a global integer.
     *
     * @param name name of the data
     * @return returns the double
     */
    public double getGlobalDouble(String name) {
        return Double.parseDouble(this.getGlobalData(name).toString());
    }

    /**
     * Gets all of the data as CSV.
     *
     * @return An array of CSV strings containing all of the data saved
     */
    public String[] getATonOfStrings() {
        String[] str = new String[this.data.size() + 1];
        int i = 0;
        str[i++] = "id" + dataSeperator + "name" + dataSeperator + "value";
        for (Map.Entry pairs : this.data.entrySet()) {
            str[i++] = ((StringPair) pairs.getKey()).one + dataSeperator + ((StringPair) pairs.getKey()).two + dataSeperator + pairs.getValue();
        }
        return str;
    }

    public static String getFormatted(String key, Object value) {
        return key + dataSeperator + value.toString();
    }

    public Map<StringPair, Object> getRaw() {
        return this.data;
    }

    /**
     * Adds a event to this dataframe.
     * 
     * @param e 
     */
    public synchronized void addEvent(Event e) {
        if (this.locked) {
            return;
        }
        this.events.add(e);
    }

    /**
     * Gets all of the events for a model.
     * 
     * @param m Model to get the events for.
     * @return List of events.
     */
    public List<Event> getEventsFor(Model m) {
        List<Event> search = new ArrayList<>();
        Iterator<Event> it = events.iterator();
        while (it.hasNext()) {
            Event e = it.next();
            if (e == null) {
                continue;
            }
            if (e.target.id == m.id) {
                search.add(e);
            }
        }
        return search;
    }

}
