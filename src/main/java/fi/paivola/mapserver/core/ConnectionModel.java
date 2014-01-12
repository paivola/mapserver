package fi.paivola.mapserver.core;

import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 * The base class for connection models.
 *
 * @author Juhani
 */
public abstract class ConnectionModel extends Model {

    public boolean passthrough;

    public ConnectionModel(int id) {
        super(id);
        this.type = "connection";
        this.passthrough = true;
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
        if (this.passthrough) {
            this.addEventToAllExceptOne(e, m);
        } else {
            super.addEvent(e, m);
        }
    }

    /**
     * Get the distance of this connection model.
     *
     * @return the distance if possible to calculate, otherwise 0
     */
    public double getLength() {
        if (this.connections.size() > 1) {
            return this.connections.get(0).distanceTo(this.connections.get(1));
        }
        return 0;
    }

}
