package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.cpu.CPU;
import br.com.leandromoreira.chip16.cpu.Memory;
import static br.com.leandromoreira.chip16.cpu.MemoryMap.*;

/**
 * @author leandro-rm
 */
public class Chip16Machine {
    public static final String URL_SPECS = "http://forums.ngemu.com/web-development-programming/138170-codename-chip16-prev-chip9.html";
    public static final String HARDWARE_SPEC = "0.8";
    public static final String CREW_SPECS = "Chris2Balls, Cottonvibes, Runawayprisoner, Serge2k, Shendo, Tykel";
    public static final String VERSION = "1.0";
    public static final String AUTHOR = "dreampeppers99";
    
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

