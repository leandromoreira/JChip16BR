package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.gpu.Render;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author leandro-rm
 */
public class Emulator implements Runnable {
    private Chip16Machine machine;
    private Render render;
    private boolean running = true;
    private final long desiredFPS = 40;
    private final long skipMaxFrames = 16;

    public Emulator(Chip16Machine machine, Render render) {
        this.machine = machine;
        this.render = render;
    }

    @Override
    public void run() {
        long excess = 0;
        long noDelays = 0;
        long overSleepTime = 0;
        while (running) {
            long beforeTime = System.currentTimeMillis();
            while (excess > desiredFPS) {
                machine.cpuStep();
                excess -= desiredFPS;
            }
            machine.cpuStep();
            machine.drawFrame(render);
            long afterTime = System.currentTimeMillis();
            long totalTime = afterTime - beforeTime;
            if (totalTime < desiredFPS) {
                sleep(desiredFPS - totalTime - overSleepTime);
                long afterSleepTime = System.currentTimeMillis();
                overSleepTime = afterSleepTime - afterTime;
                noDelays = 0;
            } else {
                overSleepTime = 0;
                excess += totalTime - desiredFPS;
                if (++noDelays == skipMaxFrames) {
                    Thread.yield();
                }
            }
        }
    }

    private void sleep(long time) {
        try {
            Thread.sleep(time);
        } catch (InterruptedException ex) {
            Logger.getLogger(Emulator.class.getName()).log(Level.SEVERE, null, ex);
            throw new IllegalStateException(ex);
        }
    }
}
