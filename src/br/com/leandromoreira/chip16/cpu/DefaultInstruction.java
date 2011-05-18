package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public class DefaultInstruction implements Instruction{
    private int sumToPC = 4;
    @Override
    public void execute(OpCodeParameter parameter) {
    }

    public void setSumToPC(int sumToPC) {
        this.sumToPC = sumToPC;
    }

    @Override
    public int addToPC() {
        return sumToPC;
    }
}
