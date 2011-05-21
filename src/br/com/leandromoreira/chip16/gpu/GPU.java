package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public interface GPU {
    int WIDTH = 320;
    int HEIGHT = 240;    
    void clear();
    boolean drawSprite(final int spriteAddress,final int xPosition,final int yPosition);
    void drawFrame(final Render render);
    short getBackgroundColor();
    Sprite getCurrentSprite();
    short[][] getFramebuffer();
    int getSpriteAddress();
    void setBackgroundColor(final short colorIndex);
    void setSprite(final short width,final short height);
}
