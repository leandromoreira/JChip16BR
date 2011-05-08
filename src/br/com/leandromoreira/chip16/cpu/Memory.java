package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public class Memory {
    private short[] memory = new short[65536];

    public short readFrom(int add) {
        return memory[add];
    }

    public void writeAt(int add, short value) {
        memory[add] = value;
    }
}
