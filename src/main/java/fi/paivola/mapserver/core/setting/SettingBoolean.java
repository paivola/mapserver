package fi.paivola.mapserver.core.setting;

import org.json.simple.JSONObject;

/**
 * Class for checkbox settings.
 * 
 * @author juhani
 */
public class SettingBoolean extends Setting {
    
    private boolean value;
    private boolean def;

    public SettingBoolean(String name) {
        super(name);
        this.type = "boolean";
        this.value = false;
        this.def = false;
    }

    @Override
    public Setting setValue(String value) {
        this.value = "true".equals(value.toLowerCase());
        return this;
    }

    @Override
    public String getValue() {
        return Boolean.toString(this.value);
    }

    @Override
    public Setting setDefault(String def) {
        this.def = "true".equals(def.toLowerCase());
        return this;
    }

    @Override
    public String toString() {
        JSONObject obj = new JSONObject();
        
        obj.put("type", this.type);
        obj.put("name", this.name);
        obj.put("value", this.value);
        obj.put("default", this.def);
        
        return obj.toString();
    }
    
}
