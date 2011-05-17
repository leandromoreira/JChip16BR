package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public class GPU {
    private final static int NUMBER_OF_COLORS = 16;
    public final static int WIDTH = 320;
    public final static int HEIGHT = 240;
    private Color[][] screen = new Color[WIDTH][HEIGHT];
    private Color backgroundColor;
    private Sprite currentSprite;
    public void clear(){
        screen = new Color[WIDTH][HEIGHT];
    }

    public void setBackgroundColor(short colorIndex) {
        backgroundColor = Colors.getColor(colorIndex);
    }

    public void setSprite(short width, short height) {
        currentSprite = new Sprite(width, height);
    }

    public void drawSprite(int sprite, int x, int y) {
        System.out.println("drw");
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color[][] getScreen() {
        return screen;
    }
}
