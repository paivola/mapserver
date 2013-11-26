package fi.paivola.mapserver.core;

public abstract class PointModel extends Model {
    
    public PointModel(int id) {
        super(id);
        this.type = "point";
    }

}
