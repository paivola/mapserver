package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingDouble;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.RangeDouble;
import java.util.Random;

/**
 * Example model extension READ THIS!
 *
 * @author juhani
 */
public class ExampleExtender extends ExtensionModel {

    public int othersAnnoyed;
    private double annoyanceEasyness;

    public ExampleExtender(int id) {
        super(id);
        this.othersAnnoyed = 0;
        this.annoyanceEasyness = 0;
    }

    /**
     * Extending models have a tick that is run after the extensions master has
     * completed it's tick and events.
     *
     * @param last
     * @param current
     */
    @Override
    public void onTick(DataFrame last, DataFrame current) {
        this.saveInt("othersAnnoyed", this.othersAnnoyed);
    }

    /**
     * This is triggered by each of the parents events.
     *
     * @param e
     */
    @Override
    public void onEvent(Event e, DataFrame current) {
        switch (e.name) {
            // Somebody else saw a cat
            case "I saw a cat!":
                // Are we annoyed?
                if ((new Random()).nextFloat() < this.annoyanceEasyness) {
                    this.addEventTo(e.sender, current, new Event("Oh thats so nice.", Event.Type.NOTIFICATION, null));
                }
                break;
            // Somebody got annoyed, yeaa!
            case "Oh thats so nice.":
                this.othersAnnoyed++;
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        sm.name = "exampleExtender"; 
        sm.exts = "examplePoint"; // what are we extending?
        // you can add new settings normally, they will belong to the parent after this.
        sm.settings.put("annoy", new SettingDouble("How easy is it to annoy this model?", 0.5, new RangeDouble(0, 1)));
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {
        this.saveInt("othersAnnoyed", othersAnnoyed);
    }

    @Override
    public void onUpdateSettings(SettingMaster sm) {
        this.annoyanceEasyness = Double.parseDouble(sm.settings.get("annoy").getValue());
    }

}
