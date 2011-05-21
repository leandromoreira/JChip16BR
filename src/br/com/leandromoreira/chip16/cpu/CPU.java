package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public interface CPU {
    boolean getFlag(final int number);
    void setFlag(final int number,final boolean value);
    int getProgramCounter();
    int getRegister(final int number);
    int getStackPointer();
    void step();
}
