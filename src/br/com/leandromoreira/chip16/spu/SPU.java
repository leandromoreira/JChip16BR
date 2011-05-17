package br.com.leandromoreira.chip16.spu;

/**
 * @author leandro-rm
 */
public interface SPU {
    void play500Mhz(int ms);
    void play1000Mhz(int ms);
    void play1500Mhz(int ms);
    void stop();
}
