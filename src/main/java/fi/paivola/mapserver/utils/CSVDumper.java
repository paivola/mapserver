package fi.paivola.mapserver.utils;

import fi.paivola.mapserver.core.GameManager;
import fi.paivola.mapserver.core.Model;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kivi
 */
public class CSVDumper {

    public static final String csv_separator = ",";
    public static final String decimal_separator = ".";

    private final List<String> lines = new LinkedList<>();
    private PrintWriter out = null;

    public CSVDumper(String run, String name) {
        try {
            File f = new File("out"+java.io.File.separator+run+java.io.File.separator+name+".csv");
            f.getParentFile().mkdirs();
            out = new PrintWriter(new FileWriter(f));
        } catch (IOException ex) {
            Logger.getLogger(CSVDumper.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     *
     * @param m The model to find
     * @param prop The name of the property such as "production"
     */
    public void add(Model m, String prop) {
        lines.add(m.id + ",\t" + prop + ",\t");
    }

    /**
     *
     * @param prop The global property to find
     */
    public void add(String prop) {
        lines.add(" ,\t" + prop + ",\t");
    }

    /**
     * Saves the data from <b>gm</b> to dump.csv
     *
     * @param gm Current GameManager
     * @param wait_for Wait for the GameManager
     */
    public void save(GameManager gm, boolean wait_for) {
        while (wait_for && !gm.isReady()) {
            try {
                Thread.sleep(1);
            } catch (InterruptedException ex) {
                Logger.getLogger(CSVDumper.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
        this.saveHead();
        Map<String, String[]> map = gm.getData();

        for (int i = 0; i < map.size(); i++) {
            saveLine(map.get(i + ""));
        }
        out.flush();
    }

    private void saveHead() {
        List<String> data = new LinkedList<>();
        for (String s : lines) {
            String id = s.split(",\t")[0];
            id = id.equals(" ") ? "global" : id;
            data.add(id + "-" + s.split(",\t")[1]);
        }
        write(data.toArray());
    }

    private void write(Object[] ss) {
        for (Object s : ss) {
            out.print((s + csv_separator).replace(".", decimal_separator));
        }
        out.println();
    }

    private void saveLine(String[] ss) {
        List<String> data = new LinkedList<>();

        for (String f : lines) {
            for (String s : ss) {
                if (s.startsWith(f)) {
                    data.add(s.split(",\t")[2]);
                }
            }
        }
        write(data.toArray());
    }
}
