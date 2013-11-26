package fi.paivola.mapserver.utils;

public class Range {

    public double s;
    public double e;

    public Range(double s, double e) {
        this.s = s;
        this.e = e;
    }

    public double clamp(double i) {
        if (this.s > i) {
            return this.s;
        }
        if (this.e < i) {
            return this.e;
        }
        return i;
    }
}
