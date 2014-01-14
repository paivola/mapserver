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

    public Model other(Model another) {
        for (Model conn : connections) {
            if (conn.id != another.id) {
                return conn;
            }
        }
        return null;
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
