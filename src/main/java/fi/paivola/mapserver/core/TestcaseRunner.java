package fi.paivola.mapserver.core;

import au.com.bytecode.opencsv.CSVReader;
import fi.paivola.mapserver.core.setting.Setting;
import fi.paivola.mapserver.core.setting.SettingMaster;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/**
 *
 * @author juhani
 */
public class TestcaseRunner {

    public class ModelE {

        public String model;
        public int line;
    }

    public class ModelA {

        public Model model;
        public String name;
        public int line;
    }

    public class LinkE {

        public int line1, line2, line3;
    }

    public class ParamE {

        public int line;
        public String what;
        public String value;
    }

    public class DefparamE {

        public String model;
        public String what;
        public String value;
    }

    private ArrayList<ModelE> modelE;
    private ArrayList<LinkE> linkE;
    private ArrayList<ParamE> paramE;
    private ArrayList<DefparamE> defparamE;

    private int ticks = (int) Math.floor(52.177457 * 20);

    public TestcaseRunner(InputStream stream) throws IOException, Exception {
        modelE = new ArrayList<>();
        linkE = new ArrayList<>();
        paramE = new ArrayList<>();
        defparamE = new ArrayList<>();
        CSVReader reader = new CSVReader(new InputStreamReader(stream));
        String[] nextLine;
        int line = 1;
        while ((nextLine = reader.readNext()) != null) {
            for (int i = 0; i < nextLine.length; i++) {
                nextLine[i] = nextLine[i].trim();
            }
            onParseLine(line++, nextLine);
        }
        run();
    }

    private void onParseLine(int line, String[] a) throws Exception {
        String act = null;
        if (a.length != 0) {
            act = a[0];
        }
        if (act.startsWith("#") || act.length() == 0) {
            return;
        }
        switch (act) {
            case "set":
                onSet(line, a);
                break;
            case "model":
                onModel(line, a);
                break;
            case "link":
                onLink(line, a);
                break;
            case "param":
                onParam(line, a);
                break;
            case "defparam":
                onDefparam(line, a);
                break;
            default:
                throw new Exception("Unknown command " + act);
        }
    }

    private void onSet(int line, String[] a) {
        if (a.length != 3) {
            return;
        }

        String what = a[1];
        String to = a[2];
        switch (what) {
            case "ticks":
                ticks = Integer.parseInt(a[2]);
                break;
            default:
                break;
        }
    }

    private void onModel(int line, String[] a) {
        if (a.length != 2) {
            return;
        }
        ModelE e = new ModelE();
        e.line = line;
        e.model = a[1];
        modelE.add(e);
    }

    private void onLink(int line, String[] a) {
        if (a.length != 4) {
            return;
        }
        LinkE e = new LinkE();
        e.line1 = Integer.parseInt(a[1]);
        e.line2 = Integer.parseInt(a[2]);
        e.line3 = Integer.parseInt(a[3]);
        linkE.add(e);
    }

    private void onDefparam(int line, String[] a) {
        if (a.length != 4) {
            return;
        }

        DefparamE e = new DefparamE();
        e.model = a[1];
        e.what = a[2];
        e.value = a[3];
        defparamE.add(e);
    }

    private void onParam(int line, String[] a) {
        if (a.length != 4) {
            return;
        }

        ParamE e = new ParamE();
        e.line = Integer.parseInt(a[1]);
        e.what = a[2];
        e.value = a[3];
        paramE.add(e);
    }

    private Model resolveLineToModel(int line, ArrayList<ModelA> models) {
        for (ModelA i : models) {
            if (i.line == line) {
                return i.model;
            }
        }
        return null;
    }

    private void resolveParams(GameManager gm, ModelA model) throws Exception {
        SettingMaster sm = gm.getDefaultSM(model.model.getClass());
        for (DefparamE e : defparamE) {
            if (e.model.equals(model.name)) {
                sm.settings.get(e.what).setValue(e.value);
            }
        }
        for (ParamE e : paramE) {
            try {
                if (e.line == model.line) {
                    Setting s = sm.settings.get(e.what);
                    if (s != null) {
                        s.setValue(e.value);
                    } else {
                        throw new Exception("Fuck");
                    }
                }
            } catch (Exception ex) {

            }
        }
        model.model.onActualUpdateSettings(sm);
    }

    private void run() throws Exception {
        GameThread one = new GameThread(ticks);
        GameManager gm = one.game;

        ArrayList<ModelA> models = new ArrayList<>();
        for (ModelE e : modelE) {
            Model a = gm.createModel(e.model);
            ModelA aa = new ModelA();
            aa.line = e.line;
            aa.name = e.model;
            aa.model = a;
            resolveParams(gm, aa);
            models.add(aa);
        }
        for (LinkE e : linkE) {
            Model m1 = resolveLineToModel(e.line1, models);
            Model m2 = resolveLineToModel(e.line2, models);
            Model m3 = resolveLineToModel(e.line3, models);
            gm.linkModelsWith(m1, m2, m3);
        }

        gm.printOnDone = 2;
        one.start();
    }
}
