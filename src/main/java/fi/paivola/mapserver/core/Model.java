package fi.paivola.mapserver.core;

import fi.paivola.mapserver.utils.Color;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * The base class for all models. Has some basic functions that you will surely
 * find useful.
 *
 * @author Juhani
 */
public abstract class Model {

    /**
     * Each model has a ID. It's used for identification and iteration.
     */
    public int id;
    /**
     * Icon is the representation on the map.
     */
    public String icon;
    /**
     * Color of the map icon
     */
    public Color color;
    /**
     * Type of the model (point, connection...)
     */
    public String type;
    public List<Model> connections;
    public List<Event> events;
    public List<Setting> settings;
    public Map<String, ExtensionModel> extensions;

    public Model(int id) {
        this.id = id;
        this.connections = new ArrayList<>();
        this.events = new ArrayList<>();
        this.settings = new ArrayList<>();
        this.extensions = new HashMap();
    }

    /**
     * Internal function that checks the events and runs extensions.
     *
     * @param last last dataframe
     * @param current current dataframe
     */
    public void onTickStart(DataFrame last, DataFrame current) {
        // lets check if there is some events waiting to get trough
        for (Event i : this.events) {
            if (i.frame == last.index) {
                this.onEvent(i);
                this.events.remove(i);
            }
        }
        this.onTick(last, current);
        for (Map.Entry pairs : this.extensions.entrySet()) {
            ((ExtensionModel) pairs.getValue())
                    .onExtensionTick(this, last, current);
        }
    }

    public void addEvent(Event e) {
        this.events.add(e);
    }

    /**
     * Adds a event to all connections.
     *
     * @param model the target of the event
     * @param frame dataframe of when it's added
     * @param e event thats added
     */
    public void addEventTo(Model model, DataFrame frame, Event e) {
        e.frame = frame.index;
        model.addEvent(e);
    }

    /**
     * Adds a event to all connections.
     *
     * @param frame dataframe of when it's added
     * @param e event thats added
     */
    public void addEventToAll(DataFrame frame, Event e) {
        e.frame = frame.index;
        for (Model i : this.connections) {
            i.addEvent(e);
        }
    }

    /**
     * Is called once the internal stuff (onTickStart) has finished, so it's
     * called once every tick.
     *
     * @param last last dataframe
     * @param current current dataframe
     */
    public abstract void onTick(DataFrame last, DataFrame current);

    /**
     * Called when there is a event to handle.
     *
     * @param e
     */
    public abstract void onEvent(Event e);

}
