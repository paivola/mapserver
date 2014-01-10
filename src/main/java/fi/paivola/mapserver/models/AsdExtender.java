package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.setting.SettingMaster;

/**
 * Example model extension READ THIS!
 * @author juhani
 */
public class AsdExtender extends ExtensionModel {

    public AsdExtender(int id, SettingMaster sm) {
        super(id, sm);
    }
    
    public AsdExtender() {
        super();
    }

    /**
     * Extending models have a special tick that is run after the extensions master has completed it's tick and events.
     * @param l
     * @param c 
     */
    @Override
    public void onExtensionTick(DataFrame l, DataFrame c) {
        this.parent.addEventToAll(c, new Event("boom2", "integer", "1"));
        //System.out.println(this.id + " (extender) sent boom2 trough " + p.id);
    }

    /**
     * This is triggered by each of the parents events.
     * @param e 
     */
    @Override
    public void onEvent(Event e, DataFrame current) {
        //System.out.println(this.id + " (extender) recieved a " + e.name);
        switch (e.name) {
            case "boom2":
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm, SettingMaster sm) {
        gm.registerExtension("asd", "extender", this.getClass()); // This registers this model as a extension to "asd"
        // temp workaround ToDo: make it better for the user
        sm.exts.add("asd"); // oh and you need to do this too
        sm.name = "asdExtender";
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {

    }

}
