package fi.paivola.mapserver.core;

import au.com.bytecode.opencsv.CSVReader;
import fi.paivola.mapserver.core.setting.Setting;
import fi.paivola.mapserver.core.setting.SettingMaster;
import fi.paivola.mapserver.utils.CSVDumper;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 *
 * @author juhani
 */
public class TestcaseRunner {

    public class ModelE {

        public ModelE(int line, String name) {
            this.name = name;
            this.line = line;
            this.alias = "";
            x = -1;
            y = -1;
        }
        public double x;
        public double y;
        public String name;
        public String alias;
        public int line;
    }

    public class ModelA {

        public ModelA(int line, String name, Model model) {
            this.line = line;
            this.model = model;
            this.name = name;
            this.alias = "";
        }
        public Model model;
        public String name;
        public String alias;
        public int line;
    }

    public class LinkE {

        public LinkE(String line1, String line2, String line3) {
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
        }
        public String line1, line2, line3;
    }

    public class ParamE {

        public ParamE(String line, String what, String value) {
            this.line = line;
            this.what = what;
            this.value = value;
        }
        public String line;
        public String what;
        public String value;
    }

    public class DefparamE {

        public DefparamE(String model, String what, String value) {
            this.model = model;
            this.what = what;
            this.value = value;
        }
        public String model;
        public String what;
        public String value;
    }

    public class WasteE {

        public WasteE(String model, String what) {
            this.model = model;
            this.what = what;
        }
        public String model;
        public String what;
    }

    public class DumpE {

        public DumpE(int line, String name) {
            this.line = line;
            this.name = name;
            this.stuff = new ArrayList<>();
        }
        public int line;
        public String name;
        public List<WasteE> stuff;
    }

    private final ArrayList<ModelE> modelE;
    private final ArrayList<LinkE> linkE;
    private final ArrayList<ParamE> paramE;
    private final ArrayList<DefparamE> defparamE;
    private final ArrayList<DumpE> dumpE;

    private int ticks = (int) Math.floor(52.177457 * 20);
    private int runs = 1;
    private int runs_done = 0;
    private String name = "unnamed";
    private String timestamp;
    private int testcase_number = 0;

    public TestcaseRunner(int testcase_number, InputStream stream) throws IOException, Exception {
        modelE = new ArrayList<>();
        linkE = new ArrayList<>();
        paramE = new ArrayList<>();
        defparamE = new ArrayList<>();
        dumpE = new ArrayList<>();
        Date date = new Date();
        //TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd_HH-mm-ss");
        //sdf.setTimeZone(tz);
        timestamp = sdf.format(date);
        this.testcase_number = testcase_number;
        CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '\"', 1);
        String[] nextLine;
        int line = 2;
        while ((nextLine = reader.readNext()) != null) {
            int size = 0;
            for (int i = 0; i < nextLine.length; i++) {
                nextLine[i] = nextLine[i].trim();
                if (nextLine[i].length() > 0) {
                    size++;
                }
            }
            String[] realLine = new String[size];
            System.arraycopy(nextLine, 0, realLine, 0, size);

            onParseLine(line++, realLine);
        }
        while (runs_done < runs) {
            run();
            runs_done++;
        }
    }

    private void onParseLine(int line, String[] a) throws Exception {
        String act = null;
        if (a.length != 0) {
            act = a[0];
        } else {
            return;
        }
        if (act == null) {
            return;
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
            case "dump":
                onDump(line, a);
                break;
            case "waste":
                onWaste(line, a);
                break;
            default:
                throw new Exception("Unknown command " + act);
        }
    }

    private void onSet(int line, String[] a) throws Exception {
        if (a.length != 3) {
            throw new Exception("Invalid amount of arguments, expected 2");
        }

        String what = a[1];
        String to = a[2];
        switch (what) {
            case "ticks":
                ticks = Integer.parseInt(a[2]);
                break;
            case "runs":
                runs = Integer.parseInt(a[2]);
                break;
            case "name":
                name = a[2];
                break;
            default:
                break;
        }
    }

    private void onModel(int line, String[] a) throws Exception {
        if (a.length == 2 || a.length == 4) {
            String s[] = a[1].split("~");
            ModelE e = new ModelE(line, s[0]);
            if (s.length == 2) {
                e.alias = s[1];
            }
            if (a.length == 4) {
                e.x = Double.parseDouble(a[2]);
                e.y = Double.parseDouble(a[3]);
            }
            modelE.add(e);
        } else {
            throw new Exception("Invalid amount of arguments, expected 1 o 3");
        }

    }

    private void onLink(int line, String[] a) throws Exception {
        if (a.length != 4) {
            throw new Exception("Invalid amount of arguments, expected 3");
        }
        LinkE e = new LinkE(a[1], a[2], a[3]);
        linkE.add(e);
    }

    private void onDefparam(int line, String[] a) throws Exception {
        if (a.length != 4) {
            throw new Exception("Invalid amount of arguments, expected 3");
        }

        DefparamE e = new DefparamE(a[1], a[2], a[3]);
        defparamE.add(e);
    }

    private void onParam(int line, String[] a) throws Exception {
        if (a.length != 4) {
            throw new Exception("Invalid amount of arguments, expected 3");
        }

        ParamE e = new ParamE(a[1], a[2], a[3]);
        paramE.add(e);
    }

    private void onDump(int line, String[] a) throws Exception {
        if (a.length != 2) {
            throw new Exception("Invalid amount of arguments, expected 1");
        }

        DumpE e = new DumpE(line, a[1]);
        dumpE.add(e);
    }

    private void onWaste(int line, String[] a) throws Exception {
        if (a.length == 3) { // global
            WasteE e = new WasteE("", a[2]);
            getDump(a[1]).stuff.add(e);
        } else if (a.length == 4) { // local
            WasteE e = new WasteE(a[2], a[3]);
            getDump(a[1]).stuff.add(e);
        } else {
            throw new Exception("Invalid amount of arguments, expected 2 or 3");
        }
    }

    private DumpE getDump(String name) throws Exception {
        for (DumpE e : dumpE) {
            if (e.name.equals(name)) {
                return e;
            }
        }
        throw new Exception("Could not find dump by name " + name);
    }

    private Model resolveLineToModel(String line, ArrayList<ModelA> models) throws Exception {
        try {
            int linen = Integer.parseInt(line);
            for (ModelA i : models) {
                if (i.line == linen) {
                    return i.model;
                }
            }
        } catch (NumberFormatException nFE) {
            for (ModelA i : models) {
                if (i.alias.equals(line)) {
                    return i.model;
                }
            }
        }

        throw new Exception("Could not find model by line " + line);
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
                if (Integer.parseInt(e.line) == model.line) {
                    Setting s = sm.settings.get(e.what);
                    if (s != null) {
                        s.setValue(e.value);
                    } else {
                        throw new Exception("Failed at getting setting named " + e.what + " from " + model.name);
                    }
                }
            } catch (NumberFormatException nFE) {
                if (e.line.equals(model.alias)) {
                    Setting s = sm.settings.get(e.what);
                    if (s != null) {
                        s.setValue(e.value);
                    } else {
                        throw new Exception("Failed at getting setting named " + e.what + " from " + model.name);
                    }
                }
            }
        }
        model.model.onActualUpdateSettings(sm);
    }

    private void run() throws Exception {
        GameThread one = new GameThread(ticks);
        GameManager gm = one.game;

        ArrayList<ModelA> models = new ArrayList<>();
        for (ModelE e : modelE) {
            Model a = gm.createModel(e.name);
            if (e.x != -1 && e.y != -1) {
                a.setLatLng(e.x, e.y);
            }
            ModelA aa = new ModelA(e.line, e.name, a);
            aa.alias = e.alias;
            resolveParams(gm, aa);
            models.add(aa);
        }
        for (LinkE e : linkE) {
            Model m1 = resolveLineToModel(e.line1, models);
            Model m2 = resolveLineToModel(e.line2, models);
            Model m3 = resolveLineToModel(e.line3, models);
            gm.linkModelsWith(m1, m2, m3);
        }
        List<CSVDumper> csv = new ArrayList<>();
        for (DumpE e : dumpE) {
            CSVDumper cs = new CSVDumper(name + "-" + testcase_number + "_" + timestamp + "/" + runs_done, e.name);
            for (WasteE ee : e.stuff) {
                if (ee.model.equals("")) { // global
                    cs.add(ee.what);
                } else {
                    cs.add(resolveLineToModel(ee.model, models), ee.what);
                }
            }
            csv.add(cs);
        }

        gm.printOnDone = 0;
        one.start();

        for (CSVDumper cs : csv) {
            cs.save(gm, true);
        }
    }
}
