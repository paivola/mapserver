package fi.paivola.mapserver.utils;

/**
 * Utility class for generating normal and non-normal distributions
 *
 * @author Jaakko Hannikainen
 */
public class Distribution {

    private double s;
    private double m;
    private double e;
    private double ka;
    private double kb;
    private double ba;
    private double bb;

    /**
     * Create a new distribution.
     *
     * @param s lower end, lower values = 0
     * @param m middle
     * @param e higher end, higher values = 0;
     */
    public Distribution(double s, double m, double e) {
        if (s == m || e == m) {
            throw new IllegalArgumentException("m must be != s and != e");
        }

        this.s = s;
        this.m = m;
        this.e = e;

        ka = 1 / (m - s);
        ba = -(s / (m - s));
        kb = 1 / (m - e);
        bb = -(e / (m - e));
    }

    /**
     * Create a new distribution.
     *
     * @param m middle
     * @param v distance from middle
     */
    public Distribution(double m, double v) {
        this(m - v, m, m + v);
    }

    /**
     * Returns a number located at a specific point.
     *
     * @param i point between lower end and higher end
     * @return value et given point
     */
    public double exact(double i) {
        if (i <= s || i >= e) {
            return 0;
        }
        if (i <= m) {
            return ka * i + ba;
        }
        return kb * i + bb;
    }

    /**
     * Returns a random number from the distribution.
     *
     * @return random number from the distribution
     */
    public double random() {
        return exact(Math.random() * (e - s) + s);
    }
}
