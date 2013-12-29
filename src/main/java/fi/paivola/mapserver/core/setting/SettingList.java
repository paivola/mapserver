package fi.paivola.mapserver.core.setting;

import java.util.ArrayList;
import java.util.List;
import org.json.simple.JSONObject;

/**
 * Class for list setting.
 * 
 * @author juhani
 */
public class SettingList extends Setting {

    private final List<String> strings;
    private int value;
    private int def;
    
    public SettingList(String name) {
        super(name);
        this.type = "list";
        this.strings = new ArrayList<>();
        this.value = 0;
        this.def = 0;
    }
    
    public SettingList(String name, ArrayList<String> strings) {
        super(name);
        this.type = "list";
        this.strings = strings;
        this.value = 0;
        this.def = 0;
    }
    
    public SettingList addOption(String option) {
        this.strings.add(option);
        return this;
    }

    @Override
    public Setting setValue(String value) {
        this.value = this.strings.indexOf(value);
        return this;
    }

    @Override
    public String getValue() {
        return this.strings.get(this.value);
    }

    @Override
    public Setting setDefault(String def) {
        this.def = this.strings.indexOf(def);
        return this;
    }

    @Override
        public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        
        obj.put("type", this.type);
        obj.put("name", this.name);
        obj.put("value", this.value);
        obj.put("default", this.def);
        obj.put("list", this.strings);
        
        return obj;
    }
    
}
