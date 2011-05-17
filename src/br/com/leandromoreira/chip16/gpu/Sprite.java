package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public class Sprite {
    private final short width;
    private final short height;

    public Sprite(short width, short height) {
        this.width = width;
        this.height = height;
    }

    public short getHeight() {
        return height;
    }

    public short getWidth() {
        return width;
    }
    
}
