package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public interface Instruction {
    void execute(final OpCodeParameter parameter);
    int addToPC();
    void setSumToPC(final int sumToPC);
}
