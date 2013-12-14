package fi.paivola.mapserver.core;

import static java.lang.Double.parseDouble;
import static java.lang.Integer.parseInt;

/**
 * Events are used for passing data in the map.
 *
 * @author Juhani
 */
public class Event {

    public String type;
    public String value;
    public String name;
    public int frame;
    public Model sender;

    public Event(String name, String type, String value) {
        this.name = name;
        this.type = type;
        this.value = value;
    }

    public int getInt() {
        return parseInt(this.value);
    }

    public String getString() {
        return this.value;
    }

    public double getDouble() {
        return parseDouble(this.value);
    }
}
