package fi.paivola.mapserver.core;

import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 * The base class for point models.
 *
 * @author Juhani
 */
public abstract class PointModel extends Model {

    public PointModel(int id, SettingMaster sm) {
        super(id, sm);
        this.type = "point";
        this.maxConnections = 9999;
    }
    
    public PointModel() {
        super();
        this.type = "point";
        this.maxConnections = 9999;
    }

}
