package fi.paivola.mapserver.core;

/**
 * The base class for connection models.
 *
 * @author Juhani
 */
public abstract class ConnectionModel extends Model {

    public boolean passtrough;

    public ConnectionModel(int id) {
        super(id);
        this.type = "connection";
        this.passtrough = true;
        this.maxConnections = 2;
    }

    /**
     * Adds a event to all connections except one.
     *
     * @param e event thats added
     * @param m model to ignore
     */
    public void addEventToAllExceptOne(Event e, Model m) {
        for (Model i : this.connections) {
            if (i != m) {
                i.addEvent(e, m);
            }
        }
    }

    @Override
    public void addEvent(Event e, Model m) {
        if (this.passtrough) {
            this.addEventToAllExceptOne(e, m);
        } else {
            super.addEvent(e, m);
        }
    }

    @Override
    public boolean linkModel(Model m) {
        if (this.connections.size() >= 2) {
            return false;
        }
        return super.linkModel(m);
    }

}
