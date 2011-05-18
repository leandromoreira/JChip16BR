package br.com.leandromoreira.chip16.gpu;

import br.com.leandromoreira.chip16.cpu.Memory;

/**
 * @author leandro-rm
 */
public class GPU {
    public final static int WIDTH = 320;
    public final static int HEIGHT = 240;
    private Color[][] screen = new Color[WIDTH][HEIGHT];
    private Color backgroundColor;
    private Sprite currentSprite;
    private final Memory memory;
    public GPU(Memory memory){
        this.memory = memory;
    }
    
    public void clear(){
        screen = new Color[WIDTH][HEIGHT];
    }

    public void setBackgroundColor(short colorIndex) {
        backgroundColor = Colors.getColor(colorIndex);
    }

    public void setSprite(short width, short height) {
        currentSprite = new Sprite(width, height);
    }

    public void drawSprite(int spriteAddress, int x, int y) {
        currentSprite.setX(x);
        currentSprite.setY(y);
        for (int row = 0; row < currentSprite.getWidth() ; row ++){
            for (int col = 0; col < currentSprite.getHeight() ; col ++){
                final Color pixColor = Colors.getColor(memory.readFrom(spriteAddress++));
                currentSprite.setPixel(row, col, pixColor);
            }
        }
    }

    public Color getBackgroundColor() {
        return backgroundColor;
    }

    public Color[][] getScreen() {
        return screen;
    }

    public Sprite getCurrentSprite() {
        return currentSprite;
    }
}
