package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.cpu.CPU;
import br.com.leandromoreira.chip16.cpu.Memory;
import br.com.leandromoreira.chip16.gpu.GPU;
import br.com.leandromoreira.chip16.rom.Chip16ROM;
import java.io.File;

/**
 * @author leandro-rm
 */
public class Chip16Machine {
    private final CPU cpu;
    private final GPU gpu;
    private final Memory memory;
    private final Chip16ROM rom;
    public Chip16Machine(final File romFile){
        memory = new Memory();
        rom = new Chip16ROM(romFile.getName(), romFile, memory);
        gpu = new GPU();
        cpu =  new CPU(memory,gpu);
    }

    public CPU getCpu() {
        return cpu;
    }

    public GPU getGpu() {
        return gpu;
    }

    public Memory getMemory() {
        return memory;
    }

    public Chip16ROM getRom() {
        return rom;
    }
}

