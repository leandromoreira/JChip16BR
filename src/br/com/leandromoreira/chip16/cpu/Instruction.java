package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public interface Instruction {
    void execute(OpCodeParameter parameter);
    String getAssembler();
    int getCycle();
}
