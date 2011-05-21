package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public class Color {

    private final int r, g, b;
    private final String description;
    private final int index;

    Color(final String colorDescription, final String colorRGB,final int index) {
        this.description = colorDescription;
        this.r = getR(colorRGB);
        this.g = getG(colorRGB);
        this.b = getB(colorRGB);
        this.index = index;
    }

    private int getR(final String rgb) {
        return Integer.valueOf(rgb.substring(rgb.length()-6, rgb.length()-4),16);
    }

    private int getG(final String rgb) {
        return Integer.valueOf(rgb.substring(rgb.length()-4, rgb.length()-2),16);
    }

    private int getB(final String rgb) {
        return Integer.valueOf(rgb.substring(rgb.length()-2, rgb.length()),16);
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

    @Override
    public String toString() {
        return description;
    }

    public int getIndex() {
        return index;
    }
}