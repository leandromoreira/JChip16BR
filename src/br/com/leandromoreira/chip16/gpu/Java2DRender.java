package br.com.leandromoreira.chip16.gpu;

import java.awt.Graphics;

/**
 * @author leandro-rm
 */
public class Java2DRender implements Render {
    private Graphics graphics;

    public Java2DRender(Graphics graphics) {
        this.graphics = graphics;
    }
    
    @Override
    public void setColor(final Color color) {
        graphics.setColor(new java.awt.Color(color.getR(), color.getG(), color.getB()));
    }

    @Override
    public void drawPixel(int x, int y) {
        graphics.drawLine(x, y, x, y);
    }
    
}
