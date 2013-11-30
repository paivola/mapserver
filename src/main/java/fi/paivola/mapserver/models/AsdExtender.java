package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;

public class AsdExtender extends ExtensionModel {

    public AsdExtender(int id) {
        super(id);
    }

    @Override
    public void onExtensionTick(DataFrame l, DataFrame c) {
        this.parent.addEventToAll(c, new Event("boom2", "integer", "1"));
        //System.out.println(this.id + " (extender) sent boom2 trough " + p.id);
    }

    @Override
    public void onEvent(Event e) {
        //System.out.println(this.id + " (extender) recieved a " + e.name);
        switch (e.name) {
            case "boom2":
                break;
            default:
                break;
        }
    }

    @Override
    public void onRegisteration(GameManager gm) {
        gm.registerExtension("asd", "extender", this.getClass());
        //System.out.println("jei");
    }

    @Override
    public void onGenerateDefaults(DataFrame df) {

    }

}
