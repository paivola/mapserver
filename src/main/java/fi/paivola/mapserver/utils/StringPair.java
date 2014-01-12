package fi.paivola.mapserver.utils;

import java.util.Objects;

/**
 *
 * @author juhani
 */
public class StringPair {
    public String one;
    public String two;
    public StringPair(String one, String two) {
        this.one = one;
        this.two = two;
    }
    @Override
    public int hashCode() {
        int hash = 1;
        hash = hash * 13 + this.one.hashCode();
        hash = hash * 31 + this.two.hashCode();
        return hash;
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final StringPair other = (StringPair) obj;
        if (!Objects.equals(this.one, other.one)) {
            return false;
        }
        return Objects.equals(this.two, other.two);
    }
}
