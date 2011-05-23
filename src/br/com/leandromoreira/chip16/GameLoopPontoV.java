package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.gpu.Render;

/**
 * STRONGLY BASED ON http://www.pontov.com.br/site/index.php/java/48-java2d/121-o-loop-de-animacao
 * By Vinícius Godoy de Mendonça
 * @author leandro-rm
 */
public class GameLoopPontoV implements Runnable {
    
    
    public static class Mensure{
        private String msg;
        private long start;
        public void init(String msg){
            this.msg = msg;
            start = System.currentTimeMillis();
        }
        public void end(){
            System.out.println(""+msg+" took "+(System.currentTimeMillis()-start)+" ms.");
        }
    }
    
    public static final int DEFAULT_UPS = 1000;
    public static final int DEFAULT_NO_DELAYS_PER_YIELD = 16;
    public static final int DEFAULT_MAX_FRAME_SKIPS = 5;
    private long desiredUpdateTime;
    private boolean running;
    private long afterTime;
    private long beforeTime = System.currentTimeMillis();
    private long overSleepTime = 0;
    private long excessTime = 0;
    private Chip16Machine machine;
    private int noDelaysPerYield = DEFAULT_NO_DELAYS_PER_YIELD;
    private int maxFrameSkips = DEFAULT_MAX_FRAME_SKIPS;
    int noDelays = 0;
    private Render render;
    
    private GameLoopPontoV(Chip16Machine machine,int ups, int maxFrameSkips, int noDelaysPerYield) {
        super();
        if (ups < 1)
            throw new IllegalArgumentException("You must display at least one frame per second!");
 
        if (ups > 1000)
            ups = 1000;
 
        this.machine = machine;
        this.desiredUpdateTime = 1000000000L / ups;
        this.running = true;
        this.maxFrameSkips = maxFrameSkips;
        this.noDelaysPerYield = noDelaysPerYield;
    }
 
    private GameLoopPontoV(Chip16Machine machine, int ups) {
        this(machine, ups, DEFAULT_MAX_FRAME_SKIPS, DEFAULT_NO_DELAYS_PER_YIELD);
    }
 
    public GameLoopPontoV(Chip16Machine machine,Render render) {
        this(machine, DEFAULT_UPS);
        this.render = render;
    }
 
    private void sleep(long nanos) {
        try {
            noDelays = 0;
            long beforeSleep = System.nanoTime();
            Thread.sleep(nanos / 1000000L, (int) (nanos % 1000000L));
            overSleepTime = System.nanoTime() - beforeSleep - nanos;
        } catch (Exception e) {}
    }
 
    private void yieldIfNeed() {
        if (++noDelays == noDelaysPerYield) {
            Thread.yield();
            noDelays = 0;
        }
    }
 
    private long calculateSleepTime() {
        return desiredUpdateTime - (afterTime - beforeTime) - overSleepTime;
    }
 
    @Override
    public void run() {
        running = true;
        try {
            while (running) {
                beforeTime = System.nanoTime();
                skipFramesInExcessTime();
                //int times = 0;
                //while (times < 261){
                    machine.cpuStep();
                  //  times++;
                //}
                machine.drawFrame(render);
                afterTime = System.nanoTime();
                long sleepTime = calculateSleepTime();
                if (sleepTime >= 0)
                   sleep(sleepTime);
               else {
                   excessTime -= sleepTime; // Sleep time is negative
                   overSleepTime = 0L;
                   yieldIfNeed();
               }
           }
       } catch (Exception e) {
            e.printStackTrace();
       } finally {
           running = false;
           System.exit(0);
       }
    }
 
    private void skipFramesInExcessTime() {
        int skips = 0;
        while ((excessTime > desiredUpdateTime) && (skips < maxFrameSkips)) {
            excessTime -= desiredUpdateTime;
            machine.cpuStep();
            skips++;
        }
    }
 
    public void stop() {
        running = false;
    }
}