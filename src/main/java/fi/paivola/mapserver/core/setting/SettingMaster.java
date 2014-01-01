package fi.paivola.mapserver.core.setting;

import fi.paivola.mapserver.utils.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
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
    
    public SettingMaster() {
        settings = new HashMap<>();
        misc = new HashMap<>();
        color = new Color(0,0,0);
        exts = new ArrayList();
        type = "";
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
        return objParent;
    }
    
    /**
     * Summons a SettingMaster from a JSON string.
     * @param JSON The JSON string.
     * @return SettingMaster, the master of all settings. Now with 100% more resurrection.
     */
    public static SettingMaster fromJSON(String JSON) {
        SettingMaster sm = new SettingMaster();
        
        
        
        return sm;
    }
}
