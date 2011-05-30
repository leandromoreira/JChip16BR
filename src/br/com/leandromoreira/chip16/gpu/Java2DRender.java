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
    public void setColor(final int index) {
        graphics.setColor(Colors.getColor(index));
    }

    @Override
    public void drawAt(int x, int y) {
        graphics.drawLine(x*2, y*2, x*2, y*2);
    }
    
}
