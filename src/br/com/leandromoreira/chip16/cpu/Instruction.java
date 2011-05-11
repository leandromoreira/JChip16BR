package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public interface Instruction {
    void execute();
    String getAssembler();
    int getCycle();
}
