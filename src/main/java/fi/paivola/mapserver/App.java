package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GameThread;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.SettingsParser;
import fi.paivola.mapserver.core.WSServer;
import fi.paivola.mapserver.utils.LatLng;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.UnknownHostException;
import org.json.simple.parser.ParseException;

public class App {

    public static void main(String[] args) throws UnknownHostException, IOException, ParseException, InterruptedException {
        
        SettingsParser sp = new SettingsParser();
        
        WSServer ws = new WSServer(parseInt(SettingsParser.settings.get("websocket_port").toString()));
        ws.start();
        
        
        BufferedReader sysin = new BufferedReader( new InputStreamReader( System.in ) );
        mainloop:
        while(true) {
            String in = sysin.readLine();
            switch(in) {
                case "q": case "quit": case "e": case "exit":
                    ws.stop();
                    break mainloop;
                case "t": case "test":
                    ws.stop();
                    runTest();
                    break mainloop;
                default:
                    break;
            }
        }
    }
    
    static void runTest() {
        
        GameThread one = new GameThread(100);
        GameManager gm = one.game;
        
        Model mg = gm.createModel("asdGlobal");
        gm.addModel(mg, "asdGlobal");

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
        
        gm.printOnDone = 1;
        
        one.start();
    }
}
