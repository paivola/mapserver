package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 * Example model extension READ THIS!
 *
 * @author juhani
 */
public class ExampleExtender extends ExtensionModel {

    public ExampleExtender(int id, SettingMaster sm) {
        super(id, sm);
    }

    public ExampleExtender() {
        super();
    }

    /**
     * Extending models have a special tick that is run after the extensions
     * master has completed it's tick and events.
     *
     * @param l
     * @param c
     */
    @Override
    public void onExtensionTick(DataFrame l, DataFrame c) {

    }

    /**
     * This is triggered by each of the parents events.
     *
     * @param e
     */
    @Override
    public void onEvent(Event e, DataFrame current) {

    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        gm.registerExtension("examplePoint", "extender", this.getClass()); // This registers this model as a extension to "asd"
        // temp workaround ToDo: make it better for the user
        sm.exts.add("examplePoint"); // oh and you need to do this too
        sm.name = "exampleExtender";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {

    }

}
