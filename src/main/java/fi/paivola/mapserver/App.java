package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;

public class App {

    public static void main(String[] args) {
        System.out.println("mapserver 0");
        GameManager gm = new GameManager(10);

        Model m1 = gm.createModel("asd");
        gm.addModel(m1, "asd");
        Model m2 = gm.createModel("asdConnection");
        gm.addModel(m2, "asdConnection");
        Model m3 = gm.createModel("asd");
        gm.addModel(m3, "asd");
        Model m4 = gm.createModel("asdConnection");
        gm.addModel(m4, "asdConnection");
        Model m5 = gm.createModel("asd");
        gm.addModel(m5, "asd");
        Model m6 = gm.createModel("asdConnection");
        gm.addModel(m6, "asdConnection");

        gm.linkModels(m1, m2);
        gm.linkModels(m2, m3);
        gm.linkModels(m3, m4);
        gm.linkModels(m4, m5);
        gm.linkModels(m5, m6);
        gm.linkModels(m6, m1);

        gm.stepTrough();
    }
}
