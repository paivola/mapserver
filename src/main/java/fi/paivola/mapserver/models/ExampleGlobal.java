package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GlobalModel;
import fi.paivola.mapserver.core.setting.Setting;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.RangeDouble;
import java.util.Random;

/**
 * Example global model READ THIS.
 *
 * @author juhani
 */
public class ExampleGlobal extends GlobalModel {

    private double luck = 0;

    public ExampleGlobal(int id) {
        super(id);
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        // This right here saves a global piece of data. Others can get it by using getGlobalData.
        current.saveGlobalData("cats", ((new Random()).nextDouble() < luck ? 1 : 0));
    }

    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "exampleGlobal";
        sm.settings.put("luck", new SettingDouble("How lucky is the world?", 0.3, new RangeDouble(0, 1)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        df.saveGlobalData("cats", 1);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        luck = Double.parseDouble(((Setting) sm.settings.get("luck")).getValue());
    }

}
