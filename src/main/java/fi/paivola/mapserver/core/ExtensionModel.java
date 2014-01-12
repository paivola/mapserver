package fi.paivola.mapserver.core;

import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 * The base class for extension models.
 *
 * @author Juhani
 */
public abstract class ExtensionModel extends Model {

    public boolean enabled;

    public ExtensionModel(int id) {
        super(id);
        this.enabled = true;
        this.type = "extension";
        this.maxConnections = 0;
    }

    @Override
    public void onTickStart(DataFrame last, DataFrame current) {

    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {

    }

    /**
     * A special onTick for extension models. Ran after master finishes.
     *
     * @param last last dataframe
     * @param current current dataframe
     */
    public abstract void onExtensionTick(DataFrame last, DataFrame current);

    /**
     * Internal function that runs some checks and calls onExtensionTick.
     *
     * @param last last dataframe
     * @param current current dataframe
     */
    public void onExtensionTickStart(DataFrame last, DataFrame current) {
        if (!this.enabled) {
            return;
        }
        this.onExtensionTick(last, current);
    }

}
