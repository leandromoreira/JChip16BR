package br.com.leandromoreira.chip16.gpu;

import br.com.leandromoreira.chip16.cpu.Memory;

/**
 * @author leandro-rm
 */
public class GPUFrameBuffer implements GPU {

    private short[][] screen = new short[WIDTH][HEIGHT];
    private short backgroundColor;
    private Sprite currentSprite;
    private final Memory memory;
    private int initAddressSprite;

    public GPUFrameBuffer(Memory memory) {
        this.memory = memory;
    }

    @Override
    public void clear() {
        screen = new short[WIDTH][HEIGHT];
    }

    @Override
    public void setBackgroundColor(short colorIndex) {
        backgroundColor = colorIndex;
    }

    @Override
    public void setSprite(short width, short height) {
        currentSprite = new Sprite(width, height);
    }

    @Override
    public boolean drawSprite(int spriteAddress, int xPosition, int yPosition) {
        boolean spriteOverlapsOther = false;
        initAddressSprite = spriteAddress;
        currentSprite.setX(xPosition);
        currentSprite.setY(yPosition);

        boolean flipFlop = true;
        for (int x = 0; x < currentSprite.getWidth(); x++) {
            for (int y = 0; y < currentSprite.getHeight(); y++) {
                final short colorIndex = (short) ((flipFlop) ? (memory.readFrom(spriteAddress) >> 4) : (memory.readFrom(spriteAddress) & 0xF));                
                if (screen[x + currentSprite.getX()][y + currentSprite.getY()] != 0) {
                     spriteOverlapsOther = true;
                }
                screen[x + currentSprite.getX()][y + currentSprite.getY()] = colorIndex;
                if (!flipFlop) {
                    spriteAddress++;
                }
                flipFlop = !flipFlop;
            }
        }
        return spriteOverlapsOther;
    }

    @Override
    public short getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public short[][] getScreen() {
        return screen;
    }

    @Override
    public Sprite getCurrentSprite() {
        return currentSprite;
    }

    @Override
    public int getSpriteAddress() {
        return initAddressSprite;
    }
}
