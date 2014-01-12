package fi.paivola.mapserver.core.setting;

import org.json.simple.JSONObject;

/**
 * Class for string settings.
 *
 * @author juhani
 */
public class SettingString extends Setting {

    private String value;
    private String def;

    public SettingString(String name) {
        super(name);
        this.type = "string";
        this.value = "";
        this.def = "";
    }

    public SettingString(String name, String value) {
        super(name);
        this.type = "string";
        this.value = value;
        this.def = "";
    }

    @Override
    public Setting setValue(String value) {
        this.value = value;
        return this;
    }

    @Override
    public String getValue() {
        return this.value;
    }

    @Override
    public Setting setDefault(String def) {
        this.def = def;
        return this;
    }

    @Override
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();

        obj.put("type", this.type);
        obj.put("name", this.name);
        obj.put("default", this.def);
        obj.put("value", this.value);

        return obj;
    }

}
