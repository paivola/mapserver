package fi.paivola.mapserver.core;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * Events are used for passing data in the map.
 *
 * @author Juhani
 */
public class Event {

    /**
     * Enumeration of the possible types.
     */
    public static enum Type {

        INT, DOUBLE, STRING, NOTIFICATION, OBJECT
    }

    /**
     * What type of event this is (?).
     */
    public Type type;
    /**
     * Value as a object.
     */
    public Object value;
    /**
     * This events name.
     */
    public String name;
    /**
     * Who sent this event?
     */
    public Model sender;
    /**
     * Target of this event.
     */
    public Model target;

    public Event(Event e) {
        this.name = e.name;
        this.type = e.type;
        this.value = e.value;
        this.target = e.target;
        this.sender = e.sender;
    }

    public Event(String name, Type type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
        this.target = null;
        this.sender = null;
    }

    public int getInt() {
        if (this.type == Type.INT) {
            return parseInt("" + this.value);
        }
        return 0;
    }

    public String getString() {
        if (this.type == Type.STRING) {
            return "" + this.value;
        }
        return null;
    }

    public double getDouble() {
        if (this.type == Type.DOUBLE) {
            return parseDouble("" + this.value);
        }
        return 0;
    }
}
