package fi.paivola.mapserver.core.setting;

import fi.paivola.mapserver.utils.RangeDouble;
import static java.lang.Double.parseDouble;
import org.json.simple.JSONObject;

/**
 * Class for double settings.
 *
 * @author juhani
 */
public class SettingDouble extends Setting {

    private double value;
    private double def;
    private RangeDouble range;

    public SettingDouble(String name) {
        super(name);
        this.type = "double";
        this.range = new RangeDouble(0.0, 17.0);
        this.value = 0.0;
        this.def = 0.0;
    }

    public SettingDouble(String name, double value, RangeDouble range) {
        super(name);
        this.type = "double";
        this.range = range;
        this.value = value;
        this.def = 0.0;
    }

    public SettingDouble setRange(double start, double end) {
        this.range = new RangeDouble(start, end);
        return this;
    }

    @Override
    public Setting setValue(String value) {
        this.value = this.range.clamp(parseDouble(value));
        return this;
    }

    @Override
    public String getValue() {
        return (new Double(this.value)).toString();
    }

    @Override
    public Setting setDefault(String def) {
        this.def = this.range.clamp(parseDouble(def));
        return this;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        obj.put("type", this.type);
        obj.put("name", this.name);
        obj.put("default", this.def);
        obj.put("value", this.value);
        obj.put("range", this.range.getJSONObject());

        return obj;
    }

}
