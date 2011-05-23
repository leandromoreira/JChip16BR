package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public interface Render {
    void setColor(final int index);
    void drawAt(final int x,final int y);
}