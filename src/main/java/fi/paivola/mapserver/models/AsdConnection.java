package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.ConnectionModel;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Color;

/**
 * Example connection model.
 * @author juhani
 */
public class AsdConnection extends ConnectionModel {

    public AsdConnection(int id, SettingMaster sm) {
        super(id, sm);
        this.passtrough = true;
    }
    
    public AsdConnection() {
        super();
        this.passtrough = true;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {

    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.color = new Color(64, 128, 256);
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {

    }

}
