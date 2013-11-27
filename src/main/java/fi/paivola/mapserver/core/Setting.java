package fi.paivola.mapserver.core;

import fi.paivola.mapserver.utils.Range;
import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * Settings are used for getting data from the UI back to the models.
 *
 * @author Juhani
 */
public class Setting {

    public String type;
    public String value;
    public String name;
    public String def;
    public Range range;

    public Setting(String name, String type, String def) {
        this.name = name;
        this.type = type;
        this.def = def;
    }

    public boolean isSet() {
        return (this.value.length() > 0);
    }

    public String getValue() {
        return (this.value.length() > 0 ? this.value : this.def);
    }

    public int getInt() {
        return parseInt(this.getValue());
    }

    public String getString() {
        return this.getValue();
    }

    public double getDouble() {
        return parseDouble(this.getValue());
    }
}
