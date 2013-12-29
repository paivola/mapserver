package fi.paivola.mapserver.utils;

import org.json.simple.JSONObject;

public class RangeInt {

    public int s;
    public int e;

    public RangeInt(int s, int e) {
        this.s = s;
        this.e = e;
    }

    public int clamp(int i) {
        if (this.s > i) {
            return this.s;
        }
        if (this.e < i) {
            return this.e;
        }
        return i;
    }
    
    @Override
    public String toString() {
        return "{min:"+this.s+",max:"+this.e+"}";
    }
    
    public JSONObject getJSONObject() {
        JSONObject obj = new JSONObject();
        obj.put("min", this.s);
        obj.put("max", this.e);
        return obj;
    }
}
