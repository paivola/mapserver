package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 * Example global model READ THIS.
 * @author juhani
 */
public class AsdGlobal extends GlobalModel {

    public AsdGlobal(int id, SettingMaster sm) {
        super(id, sm);
    }
    
    public AsdGlobal() {
        super();
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        // This right here saves a global piece of data. Others can get it by using getGlobalData.
        current.saveGlobalData("asdness", last.getGlobalInt("asdness") + 1);
    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "asdGlobal";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        df.saveGlobalData("asdness", 1);
    }

}
