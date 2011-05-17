package br.com.leandromoreira.chip16.cpu;

import java.util.Arrays;

/**
 * @author leandro-rm
 */
public class Memory {
    private static final int SIZE = 65536;
    private short[] memory = new short[SIZE];

    public short readFrom(final int address) {
        return memory[address];
    }

    public void writeAt(final int address,final short value) {
        memory[address] = value;
    }

    public void clear() {
        memory = new short[SIZE];
    }

    public long size() {
        return SIZE;
    }
    public short[] getMemoryCopy(){
        return Arrays.copyOf(memory, SIZE);
    }    
}
