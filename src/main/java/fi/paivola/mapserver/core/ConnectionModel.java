package fi.paivola.mapserver.core;

/**
 * The base class for connection models.
 *
 * @author Juhani
 */
public abstract class ConnectionModel extends Model {

    public ConnectionModel(int id) {
        super(id);
        this.type = "connection";
    }

}
