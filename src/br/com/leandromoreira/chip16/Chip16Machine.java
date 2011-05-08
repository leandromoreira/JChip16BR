package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.cpu.CPU;
import br.com.leandromoreira.chip16.cpu.Memory;
import static br.com.leandromoreira.chip16.cpu.MemoryMap.*;

/**
 * @author leandro-rm
 */
public class Chip16Machine {
    private CPU cpu;
    private Memory memory;
    public void start(){
        int address = memory.readFrom(ROM_START);
        while(true){
            cpu.step();
            address = memory.readFrom(0);
        }        
    }
}

