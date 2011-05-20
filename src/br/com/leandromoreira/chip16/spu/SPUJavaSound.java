package br.com.leandromoreira.chip16.spu;

/**
 * @author leandro-rm
 */
public class SPUJavaSound implements SPU{
    private static SyntheticSoundGenerator generator500 = new SyntheticSoundGenerator(500);
    private static SyntheticSoundGenerator generator1000 = new SyntheticSoundGenerator(1000);
    private static SyntheticSoundGenerator generator1500 = new SyntheticSoundGenerator(1500);
    @Override
    public void play500Mhz(int ms) {
        generator500.playFor(ms);
    }

    @Override
    public void play1000Mhz(int ms) {
        generator1000.playFor(ms);
    }

    @Override
    public void play1500Mhz(int ms) {
        generator1500.playFor(ms);
    }

    @Override
    public void stop() {
        stop(generator500);
        stop(generator1000);
        stop(generator1500);
    }

    private void stop(SyntheticSoundGenerator generator) {
        generator.stop();
    }
    
}
