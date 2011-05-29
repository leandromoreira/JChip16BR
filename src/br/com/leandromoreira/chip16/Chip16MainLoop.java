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
    private final static int FPS = 60;
    private final static int CPU_SPEED = 1000000;
    private final static double INSTRUCTIONS_PER_VBLANK = CPU_SPEED/FPS;
    private boolean isRunning = true;
    
    @Override
    public void run() {
        for (double instruction = 0.0; isRunning; instruction++) {
            machine.cpuStep();
            if (instruction > INSTRUCTIONS_PER_VBLANK) {
                instruction -= INSTRUCTIONS_PER_VBLANK;
                machine.drawFrame(render);
                limitSpeed();
                machine.getCpu().setFlag(0, true);
                machine.cpuStep();
                instruction++;
                machine.getCpu().setFlag(0, false);
            }
        }
    }

    void limitSpeed() {
        long startTime = System.nanoTime();
        final long nanoSeconds = 1000000000;
        final long timeLimit = nanoSeconds / 60;
        long currentTime = System.nanoTime();
        long diffirence = currentTime - startTime;
        while (diffirence < timeLimit) {
            try {
                sleep(0);
            } catch (Exception e) {
            }
            currentTime = System.nanoTime();
            diffirence = currentTime - startTime;
        }
        startTime = currentTime;
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            System.exit(0);
        }
    }
}
