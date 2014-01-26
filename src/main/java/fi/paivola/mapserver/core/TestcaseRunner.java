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
import java.util.Collections;
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
        }
        public String name;
        public int line;
    }

    public class ModelA {

        public ModelA(int line, String name, Model model) {
            this.line = line;
            this.model = model;
            this.name = name;
        }
        public Model model;
        public String name;
        public int line;
    }

    public class LinkE {

        public LinkE(int line1, int line2, int line3) {
            this.line1 = line1;
            this.line2 = line2;
            this.line3 = line3;
        }
        public int line1, line2, line3;
    }

    public class ParamE {

        public ParamE(int line, String what, String value) {
            this.line = line;
            this.what = what;
            this.value = value;
        }
        public int line;
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

        public WasteE(int model, String what) {
            this.model = model;
            this.what = what;
        }
        public int model;
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

    public TestcaseRunner(InputStream stream) throws IOException, Exception {
        modelE = new ArrayList<>();
        linkE = new ArrayList<>();
        paramE = new ArrayList<>();
        defparamE = new ArrayList<>();
        dumpE = new ArrayList<>();
        Date date = new Date();
        TimeZone tz = TimeZone.getTimeZone("UTC");
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm'Z'");
        sdf.setTimeZone(tz);
        timestamp = sdf.format(date);
        CSVReader reader = new CSVReader(new InputStreamReader(stream), ',', '\"', 1);
        String[] nextLine;
        int line = 2;
        while ((nextLine = reader.readNext()) != null) {
            int size = 0;
            for (int i = 0; i < nextLine.length; i++) {
                nextLine[i] = nextLine[i].trim();
                if(nextLine[i].length()>0) {
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

    private void onModel(int line, String[] a) {
        if (a.length != 2) {
            return;
        }
        ModelE e = new ModelE(line, a[1]);
        modelE.add(e);
    }

    private void onLink(int line, String[] a) {
        if (a.length != 4) {
            return;
        }
        LinkE e = new LinkE(Integer.parseInt(a[1]), Integer.parseInt(a[2]), Integer.parseInt(a[3]));
        linkE.add(e);
    }

    private void onDefparam(int line, String[] a) {
        if (a.length != 4) {
            return;
        }

        DefparamE e = new DefparamE(a[1], a[2], a[3]);
        defparamE.add(e);
    }

    private void onParam(int line, String[] a) {
        if (a.length != 4) {
            return;
        }

        ParamE e = new ParamE(Integer.parseInt(a[1]), a[2], a[3]);
        paramE.add(e);
    }
    
    private void onDump(int line, String[] a) {
        if (a.length != 2) {
            return;
        }
        
        DumpE e = new DumpE(line, a[1]);
        dumpE.add(e);
    }
    
    private void onWaste(int line, String[] a) {
        if (a.length == 3) { // global
            WasteE e = new WasteE(0, a[2]);
            getDump(Integer.parseInt(a[1])).stuff.add(e);
        } else if (a.length == 4) { // local
            WasteE e = new WasteE(Integer.parseInt(a[2]), a[3]);
            getDump(Integer.parseInt(a[1])).stuff.add(e);
        }
    }
    
    private DumpE getDump(int line) {
        for(DumpE e : dumpE) {
            if(e.line == line)
                return e;
        }
        return null;
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
            Model a = gm.createModel(e.name);
            ModelA aa = new ModelA(e.line, e.name, a);
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
            CSVDumper cs = new CSVDumper(name+"-"+timestamp+java.io.File.separator+runs_done, e.name);
            for (WasteE ee : e.stuff) {
                if(ee.model == 0) { // global
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
