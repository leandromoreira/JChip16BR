package br.com.leandromoreira.chip16.rom;

import br.com.leandromoreira.chip16.cpu.Memory;
import java.io.File;
import java.nio.ByteBuffer;
import static br.com.leandromoreira.chip16.util.JavaEmuUtil.*;

/**
 * @author leandro-rm
 */
public class Chip16ROM {

    private final String titleName;
    private final ByteBuffer rom;
    private final Memory memory;

    public Chip16ROM(String title, File file, Memory memory) {
        this.rom = Loader.load(file);
        this.memory = memory;
        fillMemory();
        this.titleName = title;
    }

    public Chip16ROM(File file, Memory memory) {
        this.titleName = "NO NAME";
        this.rom = Loader.load(file);
        this.memory = memory;
        fillMemory();
    }

    private void fillMemory() {
        int romSize = rom.arrayOffset();
        memory.clear();
        for (int address = 0; address < romSize; address++) {
            memory.writeAt(address, readUnsignedByte(rom));
        }
    }

    public ByteBuffer getRom() {
        return rom;
    }

    public String getTitleName() {
        return titleName;
    }
    
}
