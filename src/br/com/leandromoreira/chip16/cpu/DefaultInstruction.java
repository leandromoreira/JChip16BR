package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public abstract class DefaultInstruction implements Instruction{
    private int sumToPC = 4;

    @Override
    public void setSumToPC(int sumToPC) {
        this.sumToPC = sumToPC;
    }

    @Override
    public int addToPC() {
        return sumToPC;
    }
}
