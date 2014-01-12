package fi.paivola.mapserver.core;

import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 * The base class for extension models.
 *
 * @author Juhani
 */
public abstract class ExtensionModel extends Model {

    /**
     * Who is your daddy.
     */
    public Model parent;

    public ExtensionModel(int id) {
        super(id);
        this.parent = null;
        this.type = "extension";
        this.maxConnections = 0;
    }

    @Override
    public void onTickStart(DataFrame last, DataFrame current) {
        if (this.proto || this.needsSM) {
            return;
        }

        this.onTick(last, current);
    }

    @Override
    public void onActualRegisteration(GameManager gm, SettingMaster sm) {
        sm.type = this.type;
        this.onRegisteration(gm, sm);
        gm.registerExtension(sm.exts, this.getClass());
    }

    @Override
    public void onActualUpdateSettings(SettingMaster sm) {
        this.needsSM = false;
        if (this.name.isEmpty()) {
            this.name = sm.name;
        }
        this.onUpdateSettings(sm);
    }

}
