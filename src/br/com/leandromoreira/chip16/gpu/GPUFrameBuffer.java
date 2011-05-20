package br.com.leandromoreira.chip16.gpu;

import br.com.leandromoreira.chip16.cpu.Memory;

/**
 * @author leandro-rm
 */
public class GPUFrameBuffer implements GPU {

    private Color[][] screen = new Color[HEIGHT][WIDTH];
    private Color backgroundColor;
    private Sprite currentSprite;
    private final Memory memory;
    private int initAddressSprite;

    public GPUFrameBuffer(Memory memory) {
        this.memory = memory;
    }

    @Override
    public void clear() {
        screen = new Color[HEIGHT][WIDTH];
        for (int x = 0; x < WIDTH; x++) {
            for (int y = 0; y < HEIGHT; y++) {
                screen[y][x] = Colors.getColor(0);
            }
        }
    }

    @Override
    public void setBackgroundColor(short colorIndex) {
        backgroundColor = Colors.getColor(colorIndex);
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

        /*int startAdddress = spriteAddress;
        int endAddress = startAdddress + (currentSprite.size()/2);
        
        while (startAdddress<endAddress){
        int colorOne = memory.readFrom(startAdddress)>>4;
        int colorTwo = memory.readFrom(startAdddress)&0xF;
        System.out.print(Integer.toHexString(colorOne));
        System.out.print(Integer.toHexString(colorTwo));
        startAdddress++;
        }*/

        boolean flipFlop = true;
        for (int x = 0; x < currentSprite.getWidth(); x++) {
            for (int y = 0; y < currentSprite.getHeight(); y++) {
                int colorIndex = (flipFlop) ? (memory.readFrom(spriteAddress) >> 4) : (memory.readFrom(spriteAddress) & 0xF);
                final Color pixColor = (colorIndex == 0) ? backgroundColor : Colors.getColor(colorIndex);
                if (screen[y + currentSprite.getY()][x + currentSprite.getX()] != null) {
                    if (screen[y + currentSprite.getY()][x + currentSprite.getX()].getIndex() != 0) {
                        spriteOverlapsOther = true;
                    }
                }
                screen[y + currentSprite.getY()][x + currentSprite.getX()] = pixColor;
                if (!flipFlop) {
                    spriteAddress++;
                }
                flipFlop = !flipFlop;
            }
        }
        return spriteOverlapsOther;
    }

    @Override
    public Color getBackgroundColor() {
        return backgroundColor;
    }

    @Override
    public Color[][] getScreen() {
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
