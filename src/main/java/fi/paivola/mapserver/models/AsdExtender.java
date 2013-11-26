package fi.paivola.mapserver.models;

import fi.paivola.mapserver.core.DataFrame;
import fi.paivola.mapserver.core.Event;
import fi.paivola.mapserver.core.ExtensionModel;
import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;

public class AsdExtender extends ExtensionModel {

    public AsdExtender(int id) {
        super(id);
    }

    @Override
    public void onExtensionTick(Model p, DataFrame l, DataFrame c) {

    }

    @Override
    public void onEvent(Event e) {

    }

    @Override
    public void onRegisteration(GameManager gm) {
        gm.registerExtension("asd", "extender", this.getClass());
        System.out.println("jei");
    }

}
