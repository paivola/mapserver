package fi.paivola.mapserver.core;

/**
 * The base class for point models.
 *
 * @author Juhani
 */
public abstract class PointModel extends Model {

    public PointModel(int id) {
        super(id);
        this.type = "point";
    }

}
