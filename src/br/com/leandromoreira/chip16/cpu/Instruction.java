package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public interface Instruction {
    void execute(OpCodeParameter parameter);
    int addToPC();
    void setSumToPC(int sumToPC);
}
