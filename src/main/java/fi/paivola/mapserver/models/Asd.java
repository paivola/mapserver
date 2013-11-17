package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.MapObjectSetting;
import fi.paivola.mapserver.core.Model;

public class Asd extends Model {
    
    public static final String[] tags = {
        "asd", "point"
    };
    
    public static final String[] allowedConnections = {
        "asd_connection"
    };
    
    public static final MapObjectSetting[] settings = {
        new MapObjectSetting("conn1").setAsConnection(allowedConnections),
        new MapObjectSetting("conn2").setAsConnection(allowedConnections),
        new MapObjectSetting("volume").setAsInteger(0, 10, 5)
    };
    
    public static final Type type = Type.POINT;
    
    public Asd(long id) {
        super(id); 
    }

    @Override
    public int onInit() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int onTick(DataFrame df) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public MapObjectSetting[] getSettings() {
        return settings;
    }

    @Override
    public String[] getAllowedTags() {
        return allowedConnections;
    }
    
    @Override
    public String[] getTags() {
        return tags;
    }

    @Override
    public Type getType() {
        return type;
    }
    
}
