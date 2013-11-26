package fi.paivola.mapserver.core;

import java.util.HashMap;
import java.util.Map;

public class DataFrame {
    
    public int index;
    private final Map<String, Object> data;
    
    public DataFrame(int index) {
        this.index = index;
        this.data = new HashMap();
    }
    
    public int saveData(Model model, String field, long data) {
        if(this.data.put(model.id+"-"+field, data) == null) {
            return 0;
        }
        return 1;
    }
    
    
    
}
