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

    //1000000/60 = 16666.666666667 CPU cycles for each vblank
    // each vblank should occour each 16ms
    // scanline each 16666.666666667 / 320 = 52.083333333
    //vblank interrupt (320-240)*16666.666666667/320 = 4166.666666667 cycles
    @Override
    public void run() {
        boolean cpu_running = true;
        final double num_instructions = 1000000.0 / 60.0; // 1 mhz
        for (double x = 0.0; cpu_running; x++) {
            machine.cpuStep();
            if (x > num_instructions) {
                x -= num_instructions;
                machine.drawFrame(render);
                limitSpeed();
                machine.getCpu().setFlag(0, true);
                machine.cpuStep();
                x++;
                machine.getCpu().setFlag(0, false);
            }
        }
    }

    void limitSpeed() {
        long startTime = System.nanoTime();
        final long nSecond = 1000000000;
        final long timeLimit = nSecond / 60;
        long curTime = System.nanoTime();
        long diff = curTime - startTime;
        while (diff < timeLimit) {
            try {
                sleep(0);
            } catch (Exception e) {
            }
            curTime = System.nanoTime();
            diff = curTime - startTime;
        }
        startTime = curTime;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.exit(0);
        }
    }
}
