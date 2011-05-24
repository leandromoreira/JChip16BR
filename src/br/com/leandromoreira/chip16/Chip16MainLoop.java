package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.gpu.Render;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author leandro-rm
 */
public class Chip16MainLoop implements Runnable {

    private Chip16Machine machine;
    private Render render;

    public Chip16MainLoop(Chip16Machine machine, Render render) {
        this.machine = machine;
        this.render = render;
    }

    @Override
    public void run() {
        while (true) {
            //cpu speed 1.0MHZ
            //60HZ
            //1000000/60 = 16666.666666667 CPU [cycles VBlank will occour each]
            //320 x 240 Screen
            //16666.666666667/320 ~= 52 CPU cyles [emulation must refresh a scanline each ]
            int cycles = 0;
            while (cycles < 3) {
                machine.cpuStep();
                cycles++;
            }
            machine.drawFrame(render);
            long time = 1000/60;
            sleep(time);
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.exit(0);
        }
    }
}
