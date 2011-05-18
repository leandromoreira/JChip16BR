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
    private int initAddressSprite;
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
        currentSprite = new Sprite((short)(width*2), height);
    }

    public void drawSprite(int spriteAddress, int x, int y) {
        initAddressSprite = spriteAddress;
        currentSprite.setX(x);
        currentSprite.setY(y);
        boolean flipFlop = true;
        for (int row = 0; row < currentSprite.getWidth() ; row ++){
            for (int col = 0; col < currentSprite.getHeight() ; col ++){
                int colorIndex = (flipFlop)?(memory.readFrom(spriteAddress)>>4):(memory.readFrom(spriteAddress)&0xF);
                final Color pixColor = (colorIndex==0)?backgroundColor:Colors.getColor(colorIndex);
                currentSprite.setPixel(row, col, pixColor);
                screen[col][row] = pixColor;
                if (!flipFlop){
                    spriteAddress++;    
                }
                flipFlop = !flipFlop;
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

    public int getSpriteAddress() {
        return initAddressSprite;
    }
    
}
