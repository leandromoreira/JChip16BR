package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public class Color {

    private final int r, g, b;
    private final String description;

    Color(String colorDescription, String colorRGB) {
        this.description = colorDescription;
        this.r = getR(colorRGB);
        this.g = getG(colorRGB);
        this.b = getB(colorRGB);
    }

    private int getR(String rgb) {
        return 0;
    }

    private int getG(String rgb) {
        return 0;
    }

    private int getB(String rgb) {
        return 0;
    }

    public int getB() {
        return b;
    }

    public int getG() {
        return g;
    }

    public int getR() {
        return r;
    }

    public String getDescription() {
        return description;
    }
    
}
