package fi.paivola.mapserver.models;

import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingInt;
import fi.paivola.mapserver.core.setting.SettingList;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Icon;

public class Asd extends PointModel {

    public int boomcount;

    public Asd(int id, SettingMaster sm) {
        super(id, sm);
        this.boomcount = 0;
    }
    
    public Asd() {
        super();
        this.boomcount = 0;
    }

    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.addEventToAll(current, new Event("boom", "integer", last.getGlobalData("asdness")));
        //System.out.println(this.id + " sent boom");
        this.saveInt("booms", boomcount);
    }

    @Override
    public void onEvent(Event e) {
        switch (e.name) {
            case "boom":
                this.boomcount += e.getInt();
                //System.out.println(this.id + " recieved " + e.name + ": " + this.boomcount);
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.TOWN); // ToDo: Have some effect.
        sm.color = new Color(255, 128, 64); // What color is displayed in client.
        sm.settings.put("heepo", new SettingInt("Heepo").setRange(0, 17).setDefault("8"));
        sm.settings.put("heepo2", new SettingList("Heppa").addOption("bum").addOption("pam").setDefault("pam"));
        sm.allowedTags.add("asdConnection"); // The things trying to get connected to this need satisfy atleast one of these tags.
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.saveInt("booms", boomcount);
    }

}
