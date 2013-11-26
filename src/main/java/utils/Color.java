package utils;

public class Color {
    public int r, g, b;
    public String whole;
    
    public Color(String whole) {
        this.whole = whole;
    }
    
    public Color(int r, int g, int b) {
        this.r = r;
        this.g = g;
        this.b = b;
        this.whole = "";
    }
    
    @Override
    public String toString() {
        return (this.whole.length()>0?this.whole:"rgb("+r+", "+g+", "+b+");");
    }
}
