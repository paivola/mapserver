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
     * In what frame was this sent.
     */
    public int frame;
    /**
     * Who sent this event?
     */
    public Model sender;

    public Event(String name, Type type, Object value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public int getInt() {
        if(this.type == Type.INT)
            return parseInt("" + this.value);
        return 0;
    }

    public String getString() {
        if(this.type == Type.STRING)
            return "" + this.value;
        return null;
    }

    public double getDouble() {
        if(this.type == Type.DOUBLE)
            return parseDouble("" + this.value);
        return 0;
    }
}
