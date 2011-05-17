package br.com.leandromoreira.chip16;

import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * @author leandro-rm
 */
public class Emulator implements Runnable {

    private Chip16Machine machine;
    private long beforeTime;
    private long excessTime = 0;
    private long desiredUpdateTime;
    private final long UPS = 60;
    private long afterTime;
    private long overSleepTime;

    public Emulator() {
        this.desiredUpdateTime = 1000000000L / UPS;
    }

    private long calculateSleepTime() {
        return desiredUpdateTime - (afterTime - beforeTime) - overSleepTime;
    }

    private void sleep(long nanos) {
        try {
            long beforeSleep = System.nanoTime();
            Thread.sleep(nanos / 1000000L, (int) (nanos % 1000000L));
            overSleepTime = System.nanoTime() - beforeSleep - nanos;
        } catch (Exception e) {
        }
    }

    @Override
    public void run() {
        while (true) {
            beforeTime = System.nanoTime();
            while ((excessTime > desiredUpdateTime)) {
                excessTime -= desiredUpdateTime;
                machine.cpuStep();
            }
            machine.cpuStep();
            afterTime = System.nanoTime();
            long sleepTime = calculateSleepTime();
            if (sleepTime >= 0) {
                sleep(sleepTime);
            } else {
                excessTime -= sleepTime; // Sleep time is negative
                overSleepTime = 0L;
                Thread.yield();
            }
            machine.raiseVBlank();
        }
    }
}
