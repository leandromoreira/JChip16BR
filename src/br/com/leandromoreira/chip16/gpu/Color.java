package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public class Color {
    private final int r,g,b;
    Color(int r,int g, int b){
        this.r = r;
        this.g = g;
        this.b = b;
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
}
