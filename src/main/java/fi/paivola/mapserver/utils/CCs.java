package fi.paivola.mapserver.utils;

import fi.paivola.mapserver.core.setting.SettingMaster;
import java.util.HashMap;
import java.util.Map;

/**
 * ClassClasses, don't ask (java why no structs)
 *
 * @author Juhani
 */
public class CCs {

    public Class cls;
    public Map<String, Object> clss;
    public SettingMaster sm;
    public String misc;

    public CCs(Class cls) {
        this.cls = cls;
        this.clss = new HashMap<>();
        this.sm = null;
    }
}
