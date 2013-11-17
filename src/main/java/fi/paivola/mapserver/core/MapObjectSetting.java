package fi.paivola.mapserver.core;

public class MapObjectSetting {
    public String name;
    public String type;
    public long min;
    public long max;
    public long def;
    public long value;
    public String[] validConnections;
    
    public MapObjectSetting (String name) {
        this.name = name;
        this.type = "long";
    }
    
    public MapObjectSetting setAsInteger(long min, long max, long def) {
        this.type = "integer";
        this.min = min;
        this.max = max;
        this.def = def;
        return this;
    }
    
    public MapObjectSetting setName(String name) {
        this.name = name;
        return this;
    }
    public MapObjectSetting setType(String type) {
        this.type = type;
        return this;
    }
    public MapObjectSetting setMin(long min) {
        this.min = min;
        return this;
    }
    public MapObjectSetting setMax(long max) {
        this.max = max;
        return this;
    }
    public MapObjectSetting setDefault(long def) {
        this.def = def;
        return this;
    }
    public MapObjectSetting setValue(long val) {
        this.value = val;
        return this;
    }
    public MapObjectSetting setAsConnection(String[] validConnections) {
        this.type = "connection";
        this.validConnections = validConnections;
        return this;
    }
    

}
