package br.com.leandromoreira.chip16.rom;

import br.com.leandromoreira.chip16.cpu.Memory;
import java.io.File;
import java.nio.ByteBuffer;
import static br.com.leandromoreira.chip16.util.JavaEmuUtil.*;
import static br.com.leandromoreira.chip16.cpu.MemoryMap.*;

/**
 * @author leandro-rm
 */
public class Chip16ROM {

    private final String titleName;
    private final ByteBuffer rom;
    private final Memory memory;

    public Chip16ROM(String title, File file, Memory memory) {
        this.titleName = title;
        this.rom = Loader.load(file);
        this.memory = memory;
        fillMemory();
    }

    public Chip16ROM(File file, Memory memory) {
        this.titleName = "NO NAME";
        this.rom = Loader.load(file).asReadOnlyBuffer();
        this.memory = memory;
        fillMemory();
    }

    private void fillMemory() {
        int romSize = rom.arrayOffset();
        memory.clear();
        for (int address = 0; address < romSize; address++) {
            if (address > ROM_END){
                throw new IllegalStateException("This ROM exceds the max size of Chip16! Max lenght: "+ROM_END+" bytes.");
            }
            memory.writeAt(ROM_START+address, readUnsignedByte(rom));
        }
    }

    public ByteBuffer getRom() {
        return rom;
    }

    public String getTitleName() {
        return titleName;
    }
    
}
