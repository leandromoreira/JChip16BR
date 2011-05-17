package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public class Sprite {
    private final short width;
    private final short height;
    private int x,y;
    private Color[][] sprite;

    public Sprite(short width, short height) {
        this.width = width;
        this.height = height;
        this.sprite = new Color[width][height];
    }
    public void setPixel(int row, int col, Color color){
        sprite[row][col] = color;
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
    
    public Color[][] getPixels(){
        return sprite;
    }
}
