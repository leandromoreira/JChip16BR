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

    public GPUFrameBuffer(final Memory memory) {
        this.memory = memory;
    }

    @Override
    public void clear() {
        screen = new short[WIDTH][HEIGHT];
    }

    @Override
    public void setBackgroundColor(final short colorIndex) {
        backgroundColor = colorIndex;
    }

    @Override
    public void setSprite(final short width,final short height) {
        currentSprite = new Sprite(width, height);
    }

    @Override
    public boolean drawSprite(int spriteAddress,final int xPosition,final int yPosition) {
        boolean spriteOverlapsOther = false;
        initAddressSprite = spriteAddress;
        currentSprite.setX(xPosition);
        currentSprite.setY(yPosition);
        boolean flipFlop = true;
        for (int y = 0; y < currentSprite.getHeight(); y++) {
            for (int x = 0; x < currentSprite.getWidth(); x++) {
                final short colorIndex = (short) ((flipFlop) ? (memory.readFrom(spriteAddress) >> 4) : (memory.readFrom(spriteAddress) & 0xF));                
                if (thereIsAnotherSpriteAt(x, y)) {
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

    private boolean thereIsAnotherSpriteAt(final int x,final int y) {
        return screen[x + currentSprite.getX()][y + currentSprite.getY()] != 0;
    }

    @Override
    public short getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public short[][] getFramebuffer() {
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

    @Override
    public void drawFrame(final Render render) {
        for (int y = 0; y < GPU.HEIGHT ; y++){
            for (int x = 0 ; x < GPU.WIDTH ; x++){
                if (screen[x][y]!=0){
                    render.setColor(screen[x][y]);
                    render.drawAt(x, y);
                }else{
                    render.setColor(getBackgroundColor());
                    render.drawAt(x, y);
                }
            }
        }
    }
}