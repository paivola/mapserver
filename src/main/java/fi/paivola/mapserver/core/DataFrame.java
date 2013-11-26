package fi.paivola.mapserver.core;

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
    private final Map<String, Object> data;

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
        return this.data.put(model.id + "-" + name, data) == null;
    }

    /**
     * Gets all of the data as CSV.
     *
     * @return An array of CSV strings containing all of the data saved
     */
    public String[] getATonOfStrings() {
        String[] str = new String[this.data.size()];
        int i = 0;
        for (Map.Entry pairs : this.data.entrySet()) {
            str[i++] = pairs.getKey().toString().replace("-", ", ") + ", "
                    + pairs.getValue();
        }
        return str;
    }

}
