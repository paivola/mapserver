package fi.paivola.mapserver.core;

/**
 * The base class for global models.
 *
 * @author juhani
 */
public abstract class GlobalModel extends Model {

    public GlobalModel(int id) {
        super(id);
        this.maxConnections = 0;
        this.type = "global";
    }

}
