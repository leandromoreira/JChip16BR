package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.gpu.Render;

/**
 * @author leandro-rm
 */
public class Chip16MainLoop implements Runnable {

    private Chip16Machine machine;
    private Render render;
    private final static int FPS = 60;
    private final static int CPU_SPEED = 1000000;
    private final static double INSTRUCTIONS_PER_VBLANK = CPU_SPEED / FPS;
    private final long NANO_SECONDS = 1000000000;
    private final long TIME_LIMIT = NANO_SECONDS / FPS;
    private boolean isRunning = true;
    private long startTime;
    private long currentTime;
    private long diffirence;

    public Chip16MainLoop(Chip16Machine machine, Render render) {
        this.machine = machine;
        this.render = render;
    }

    @Override
    public void run() {
        for (double instruction = 0.0; isRunning; instruction++) {
            machine.cpuStep();
            if (instruction > INSTRUCTIONS_PER_VBLANK) {
                instruction -= INSTRUCTIONS_PER_VBLANK;
                machine.drawFrame(render);
                limitSpeed();
                machine.raiseVBlank();
                machine.cpuStep();
                machine.resetVBlank(); 
                instruction++;
            }
        }
    }

    private void limitSpeed() {
        startTime = System.nanoTime();
        currentTime = System.nanoTime();
        diffirence = currentTime - startTime;
        while (diffirence < TIME_LIMIT) {
            sleep(0);
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
