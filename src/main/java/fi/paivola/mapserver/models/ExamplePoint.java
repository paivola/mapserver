package fi.paivola.mapserver.models;

import fi.paivola.mapserver.utils.Color;
import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.PointModel;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.Icon;
import fi.paivola.mapserver.utils.RangeDouble;
import java.util.Random;

/**
 * Example point model READ THIS PLEASE.
 *
 * @author juhani
 */
public class ExamplePoint extends PointModel {

    public int catSightings;
    private double skill;

    /**
     * Constructor for instances of this model.
     *
     * @param id just pass it around.
     */
    public ExamplePoint(int id) {
        super(id);
        this.catSightings = 0;
        this.skill = 0;
    }

    /**
     * This is executed each frame of the simulation.
     *
     * @param last last dataframe, where you get data from (such as globals)
     * @param current current dataframe, you write data for exporting here
     */
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        /* These cat's are quite strange. 
         If there are cats in the world, there is a chance of atleast 
         one of them being atleast here. What a strange world do we live in.
         */
        if (last.getGlobalInt("cats") > 0 && (new Random()).nextFloat() < this.skill) {
            // If we saw a cat, let's tell our neighbours about it!
            this.addEventToAll(current, new Event("I saw a cat!", Event.Type.NOTIFICATION, null));
            this.catSightings++;
        }

        this.saveInt("catsSeen", catSightings);
    }

    /**
     * If you have a event waiting, this is run before onTick for each of those
     * events.
     *
     * @param e
     */
    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    /**
     * This is run once, define constant stuff here (setting master most
     * notably)
     *
     * @param gm
     * @param sm
     */
    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.setIcon(Icon.CAPE); // ToDo: Have some effect.
        sm.color = new Color(255, 204, 255); // What color is displayed in client.
        sm.settings.put("skill", new SettingDouble("How skillfull is this model?", 0.75, new RangeDouble(0, 1)));
        sm.allowedNames.add("exampleConnection"); // The things trying to get connected to this need satisfy atleast one of these tags.
        sm.name = "examplePoint";
    }

    /**
     * Here you are supposed to handle generating the default values for each
     * variable, or something.
     *
     * @param df
     */
    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.saveInt("catsSeen", catSightings);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        this.skill = Double.parseDouble(sm.settings.get("skill").getValue());
    }

}
