package fi.paivola.mapserver.core;

import utils.Color;
import java.util.ArrayList;
import java.util.List;

public abstract class Model {
    
    public int id;
    public String icon;
    public Color color;
    public String type;
    public List<Model> connections;
    public List<Event> events;
    public List<Setting> settings;
    
    public Model(int id) {
        this.id = id;
        this.connections = new ArrayList<>();
        this.events = new ArrayList<>();
        this.settings = new ArrayList<>();
    }
    
    public void tickStart(DataFrame last, DataFrame current) {
        // lets check if there is some events waiting to get trough
        for(Event i : this.events) {
             if(i.frame == last.index) {
                 this.onEvent(i);
                 this.events.remove(i);
             }
        }
        this.onTick(last, current);
    }
    
    public void addEvent(Event e) {
        this.events.add(e);
    }
    
    public void addEventTo(Model m, DataFrame dat, Event e) {
        e.frame = dat.index;
        m.addEvent(e);
    }
    
    public void addEventToAll(DataFrame dat, Event e) {
        e.frame = dat.index;
        for(Model i : this.connections) {
            i.addEvent(e);
        }
    }
    
    public abstract void onTick(DataFrame last, DataFrame current);
    public abstract void onEvent(Event e);

}