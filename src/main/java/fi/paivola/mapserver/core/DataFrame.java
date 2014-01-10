package fi.paivola.mapserver.core;

import fi.paivola.mapserver.utils.StringPair;
import java.util.HashMap;
import java.util.Map;

/**
 * DataFrame is used for saving data permanently. Either for other models to get
 * it or for exporting to CSV.
 *
 * @author Juhani
 */
public class DataFrame {

    public int index;
    public boolean locked;
    private final Map<StringPair, Object> data;
    private static final String dataSeperator = "\t";

    /**
     * Class constructor.
     *
     * @param index the number of this frame
     */
    public DataFrame(int index) {
        this.index = index;
        this.data = new HashMap();
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
    public boolean saveGlobalData(String name, String data) {
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
    public String getGlobalData(String name) {
        return this.data.get(new StringPair(" ", name)).toString();
    }

    /**
     * Gets all of the data as CSV.
     *
     * @return An array of CSV strings containing all of the data saved
     */
    public String[] getATonOfStrings() {
        String[] str = new String[this.data.size()+1];
        int i = 0;
        str[i++] = "id"+dataSeperator+"name"+dataSeperator+"value";
        for (Map.Entry pairs : this.data.entrySet()) {
            str[i++] = ((StringPair)pairs.getKey()).one + dataSeperator + ((StringPair)pairs.getKey()).two + dataSeperator + pairs.getValue();
        }
        return str;
    }
    
    public static String getFormatted(String key, Object value) {
        return key + dataSeperator + value.toString();
    }

    public Map<StringPair, Object> getRaw() {
        return this.data;
    }

}
