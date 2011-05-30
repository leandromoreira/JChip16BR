package br.com.leandromoreira.chip16.gpu;

import java.awt.Graphics;

/**
 * @author leandro-rm
 */
public class Java2DRender implements Render {
    private Graphics graphics;
    private int size = 2;

    public Java2DRender(Graphics graphics) {
        this.graphics = graphics;
    }
    
    @Override
    public void setColor(final int index) {
        graphics.setColor(Colors.getColor(index));
    }

    @Override
    public void drawAt(int x, int y) {
        graphics.drawLine(x*size, y*size, x*size, y*size);
    }
    
}
