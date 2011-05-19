package br.com.leandromoreira.chip16.gpu;

import br.com.leandromoreira.chip16.cpu.Memory;

/**
 * @author leandro-rm
 */
public class GPU {
    public final static int WIDTH = 320;
    public final static int HEIGHT = 240;
    private Color[][] screen = new Color[HEIGHT][WIDTH];
    private Color backgroundColor;
    private Sprite currentSprite;
    private final Memory memory;
    private int initAddressSprite;
    public GPU(Memory memory){
        this.memory = memory;
    }
    
    public void clear(){
        screen = new Color[HEIGHT][WIDTH];
    }

    public void setBackgroundColor(short colorIndex) {
        backgroundColor = Colors.getColor(colorIndex);
    }

    public void setSprite(short width, short height) {
        currentSprite = new Sprite(width, height);
    }

    public boolean drawSprite(int spriteAddress, int xPosition, int yPosition) {
        boolean spriteOverlapsOther = false;
        initAddressSprite = spriteAddress;
        currentSprite.setX(xPosition);
        currentSprite.setY(yPosition);
        boolean flipFlop = false;
        for (int x = 0; x < currentSprite.getWidth() ; x ++){
            for (int y = 0; y < currentSprite.getHeight() ; y ++){
                int colorIndex = (flipFlop)?(memory.readFrom(spriteAddress)&0xF):(memory.readFrom(spriteAddress)>>4);
                final Color pixColor = (colorIndex==0)?backgroundColor:Colors.getColor(colorIndex);
                if (screen[y+currentSprite.getY()][x+currentSprite.getX()] != null){
                    if (screen[y+currentSprite.getY()][x+currentSprite.getX()].getIndex()!=0){
                        spriteOverlapsOther = true;
                    }
                }
                screen[y+currentSprite.getY()][x+currentSprite.getX()] = pixColor;
                if (!flipFlop){
                    spriteAddress++;    
                }
                flipFlop = !flipFlop;
            }
        }
        return spriteOverlapsOther;
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
