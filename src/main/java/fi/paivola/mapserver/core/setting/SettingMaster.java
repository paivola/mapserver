package fi.paivola.mapserver.core.setting;

import fi.paivola.mapserver.utils.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

/**
 * Manages transferring Settings between model prototypes, the interface and the final models.
 * 
 * @author juhani
 */
public class SettingMaster {

    /**
     * All of the settings.
     */
    public Map<String, Setting> settings;
    /**
     * Misc settings, such as icon.
     */
    public Map<String, String> misc;
    /**
     * Color.
     */
    public Color color;
    /**
     * Type.
     */
    public String type;
    /**
     * Names of the things this extends.
     */
    public List<String> exts;
    /**
     * Names that are allowed to be connected.
    */
    public List<String> allowedNames;
    /**
     * Name of the model.
     */
    public String name;
    
    public SettingMaster() {
        settings = new HashMap<>();
        misc = new HashMap<>();
        color = new Color(0,0,0);
        exts = new ArrayList();
        allowedNames = new ArrayList();
        type = "";
        name = "";
    }
    
    /**
     * Sets a icon.
     * @param iconName Icons name, you can use Icon.TOWN etc
     */
    public void setIcon(String iconName) {
        this.misc.put("icon", iconName);
    }
    
    /**
     * 
     * @return Gets a JSON string from the settings.
     */
    @Override
    public String toString() {
        return this.getJSONObject().toJSONString();
    }
    
    public JSONObject getJSONObject() {
        JSONObject objParent = new JSONObject();
        JSONObject objSettings = new JSONObject(this.settings);
        JSONObject objMisc = new JSONObject(this.misc);
        objParent.put("settings", objSettings);
        objParent.put("misc", objMisc);
        objParent.put("color", this.color.toString());
        objParent.put("extends", this.exts);
        objParent.put("type", this.type);
        objParent.put("allowedNames", this.allowedNames);
        objParent.put("name", this.name);
        return objParent;
    }
    
    /**
     * Summons a SettingMaster from a JSON string.
     * @param obj The JSON object.
     * @return SettingMaster, the master of all settings. Now with 100% more resurrection.
     */
    public static SettingMaster fromJSON(JSONObject obj) {
        SettingMaster sm = new SettingMaster();
        
        sm.misc.putAll(((JSONObject)obj.get("misc")));
        sm.settings.putAll(((JSONObject)obj.get("settings")));
        sm.color = new Color(obj.get("color").toString());
        sm.exts.addAll((JSONArray)obj.get("extends"));
        sm.type = obj.get("type").toString();
        sm.allowedNames.addAll((JSONArray)obj.get("allowedNames"));
        sm.name = obj.get("name").toString();
        
        return sm;
    }
}
