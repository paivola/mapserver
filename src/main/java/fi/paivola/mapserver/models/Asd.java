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

/**
 * Example point model READ THIS PLEASE.
 * @author juhani
 */
public class Asd extends PointModel {

    /**
     * Just a int.
     */
    public int boomcount;

    /**
     * Constructor, this is needed!
     * @param id just pass it around.
     * @param sm theoretically you would get your settings from this.
     */
    public Asd(int id, SettingMaster sm) {
        super(id, sm);
        this.boomcount = 0;
    }
    
    public Asd() {
        super();
        this.boomcount = 0;
    }

    /**
     * This is executed each frame of the simulation.
     * @param last last dataframe, where you get data from (such as globals)
     * @param current current dataframe, you write data for exporting here
     */
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.addEventToAll(current, new Event("boom", "integer", last.getGlobalData("asdness")));
        //System.out.println(this.id + " sent boom");
        this.saveInt("booms", boomcount);
    }

    /**
     * If you have a event waiting, this is run before onTick for each of those events.
     * @param e 
     */
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

    /**
     * This is run once, define constant stuff here (setting master most notably)
     * @param gm
     * @param sm 
     */
    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.TOWN); // ToDo: Have some effect.
        sm.color = new Color(255, 128, 64); // What color is displayed in client.
        sm.settings.put("heepo", new SettingInt("Heepo").setRange(0, 17).setDefault("8"));
        sm.settings.put("heepo2", new SettingList("Heppa").addOption("bum").addOption("pam").setDefault("pam"));
        sm.allowedTags.add("asdConnection"); // The things trying to get connected to this need satisfy atleast one of these tags.
    }

    /**
     * Here you are supposed to handle generating the default values for each variable, or something.
     * @param df 
     */
    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.saveInt("booms", boomcount);
    }

}
