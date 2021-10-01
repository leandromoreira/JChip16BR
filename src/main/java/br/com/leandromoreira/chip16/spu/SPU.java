package br.com.leandromoreira.chip16.spu;

/**
 * @author leandro-rm
 */
public interface SPU {
    void play500Mhz(final int ms);
    void play1000Mhz(final int ms);
    void play1500Mhz(final int ms);
    void stop();
}
