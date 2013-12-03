package fi.paivola.mapserver.core.setting;

import fi.paivola.mapserver.utils.Range;
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
    private Range range;
    
    public SettingDouble(String name) {
        super(name);
        this.type = "double";
        this.range = new Range(0.0, 17.0);
        this.value = 0.0;
        this.def = 0.0;
    }
    
    public SettingDouble setRange(double start, double end) {
        this.range = new Range(start, end);
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
    public String toString() {
        JSONObject obj = new JSONObject();
        
        obj.put("type", this.type);
        obj.put("name", this.name);
        obj.put("default", this.def);
        obj.put("value", this.value);
        obj.put("range", this.range.toString());
        
        return obj.toString();
    }
    
}
