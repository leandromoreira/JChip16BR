package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public interface Render {
    void setColor(final Color color);
    void drawAt(final int x,final int y);
}