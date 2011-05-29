package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.gpu.Render;

/**
 * @author leandro-rm
 * All the code from http://forums.ngemu.com/web-development-programming/138170-codename-chip16-prev-chip9-20.html#post1970906
 * Thanks for cottonvibes http://forums.ngemu.com/members/100638.html
 * and Bill_gates http://forums.ngemu.com/members/30482.html
 */
public class Chip16MainLoop implements Runnable {

    private Chip16Machine machine;
    private Render render;
    private final static int FPS = 60;
    private final static int CPU_SPEED = 1000000;
    private final static long INSTRUCTIONS_PER_VBLANK = CPU_SPEED / FPS;
    private final static long NANO_SECONDS = 1000000000;
    private final static long TIME_LIMIT = NANO_SECONDS / FPS;
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
        for (long instructionCounter = 0; isRunning; instructionCounter++) {
            machine.cpuStep();
            if (instructionCounter > INSTRUCTIONS_PER_VBLANK) {
                instructionCounter -= INSTRUCTIONS_PER_VBLANK;
                machine.drawFrame(render);
                limitSpeed();
                machine.raiseVBlank();
                machine.cpuStep();
                machine.resetVBlank(); 
                instructionCounter++;
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
