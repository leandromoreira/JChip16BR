package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public interface GPU {
    int WIDTH = 320;
    int HEIGHT = 240;    
    void clear();
    boolean drawSprite(int spriteAddress, int xPosition, int yPosition);
    short getBackgroundColor();
    Sprite getCurrentSprite();
    short[][] getFramebuffer();
    int getSpriteAddress();
    void setBackgroundColor(short colorIndex);
    void setSprite(short width, short height);
}
