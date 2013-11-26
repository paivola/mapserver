package fi.paivola.mapserver.utils;

import java.util.HashMap;
import java.util.Map;

/**
 * ClassClasses, don't ask (java why no structs)
 *
 * @author Juhani
 */
public class CCs {

    public Object cls;
    public Map<String, Object> clss;

    public CCs(Object cls) {
        this.cls = cls;
        this.clss = new HashMap<>();
    }
}
