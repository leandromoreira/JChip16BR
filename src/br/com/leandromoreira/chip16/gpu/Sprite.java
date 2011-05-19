package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public class Sprite {
    private final short width;
    private final short height;
    private int x,y;

    public Sprite(short width, short height) {
        this.width = (short) (width * 2);
        this.height = height;
    }
    public short getHeight() {
        return height;
    }

    public short getWidth() {
        return width;
    }

    public void setX(int x) {
        this.x = x;
    }

    public void setY(int y) {
        this.y = y;
    }

    public int getX() {
        return x;
    }

    public int getY() {
        return y;
    }

    public int size() {
        return getWidth() * getHeight();
    }

    @Override
    public String toString() {
        return "Sprite{" + "width=" + width + ", height=" + height + ", x=" + x + ", y=" + y + '}';
    }
    
}
