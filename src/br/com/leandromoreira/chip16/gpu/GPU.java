package br.com.leandromoreira.chip16.gpu;

/**
 * @author leandro-rm
 */
public interface GPU {
    int HEIGHT = 240;
    int WIDTH = 320;
    void clear();
    boolean drawSprite(int spriteAddress, int xPosition, int yPosition);
    short getBackgroundColor();
    Sprite getCurrentSprite();
    short[][] getScreen();
    int getSpriteAddress();
    void setBackgroundColor(short colorIndex);
    void setSprite(short width, short height);
}
