package fi.paivola.mapserver;

import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.GameThread;
import fi.paivola.mapserver.core.Model;
import fi.paivola.mapserver.core.SettingsParser;
import fi.paivola.mapserver.core.WSServer;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import static java.lang.Integer.parseInt;
import java.net.UnknownHostException;
import java.util.logging.LogManager;
import org.json.simple.parser.ParseException;

public class App {

    static final boolean profilingRun = false;

    public static void main(String[] args) throws UnknownHostException, IOException, ParseException, InterruptedException {

        SettingsParser.parse();

        if (profilingRun) { // For profiling

            LogManager.getLogManager().reset();

            for (int i = 0; i < 1000; i++) {
                runTest();
            }

        } else {

            WSServer ws = new WSServer(parseInt(SettingsParser.settings.get("websocket_port").toString()));
            ws.start();

            BufferedReader sysin = new BufferedReader(new InputStreamReader(System.in));
            printHelp();
            mainloop:
            while (true) {
                String in = sysin.readLine();
                switch (in) {
                    case "q":
                    case "quit":
                    case "e":
                    case "exit":
                        ws.stop();
                        break mainloop;
                    case "t":
                    case "test":
                        ws.stop();
                        runTest();
                        break mainloop;
                    case "h":
                    case "help":
                        printHelp();
                        break;
                    default:
                        System.out.println("Unknown command (" + in + ")");
                        printHelp();
                        break;
                }
            }
        }
    }

    static void printHelp() {
        System.out.println("q|e|quit|exit   - Quits the program\n"
                + "t|test          - Run the test function\n"
                + "h|help          - Display this help");
    }

    /**
     * This function can be used for testing your own models. Please modify
     * this!
     */
    static void runTest() {

        // How many ticks? Each one is a week.
        GameThread one = new GameThread((int) Math.floor(52.177457 * 20));
        GameManager gm = one.game;

        // Create and add
        Model mg = gm.createModel("exampleGlobal");
        gm.addModel(mg, "exampleGlobal");

        Model m1 = gm.createModel("examplePoint");
        gm.addModel(m1, "examplePoint");
        Model m2 = gm.createModel("exampleConnection");
        gm.addModel(m2, "exampleConnection");
        Model m3 = gm.createModel("examplePoint");
        gm.addModel(m3, "examplePoint");
        Model m4 = gm.createModel("exampleConnection");
        gm.addModel(m4, "exampleConnection");
        Model m5 = gm.createModel("examplePoint");
        gm.addModel(m5, "examplePoint");
        Model m6 = gm.createModel("exampleConnection");
        gm.addModel(m6, "exampleConnection");

        // And link!
        gm.linkModels(m1, m2);
        gm.linkModels(m2, m3);
        gm.linkModels(m3, m4);
        gm.linkModels(m4, m5);
        gm.linkModels(m5, m6);
        gm.linkModels(m6, m1);

        // Print final data in the end?
        if (!profilingRun) {
            gm.printOnDone = 2;
        }

        // Start the gamethread
        one.start();
    }
}
