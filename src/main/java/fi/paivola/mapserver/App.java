package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameManager;

public class App {

    public static void main(String[] args) {
        System.out.println("mapserver 0");
        GameManager gm = new GameManager(10);
        gm.createModel("asd");
        gm.createModel("asd");
    }
}
