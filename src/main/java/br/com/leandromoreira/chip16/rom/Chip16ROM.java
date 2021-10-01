package br.com.leandromoreira.chip16.rom;

import br.com.leandromoreira.chip16.cpu.Memory;
import java.io.File;
import java.nio.ByteBuffer;
import static br.com.leandromoreira.chip16.util.JavaEmuUtil.*;
import static br.com.leandromoreira.chip16.cpu.MemoryMap.*;

/**
 * @author leandro-rm
 */
public class Chip16ROM implements ROM {

    private final String titleName;
    private final ByteBuffer rom;
    private final Memory memory;
    private final long length;

    public Chip16ROM(final String title,final File file,final Memory memory) {
        this.titleName = title;
        this.rom = Loader.load(file);
        length = file.length();
        this.memory = memory;
        fillMemory();
    }

    public Chip16ROM(final File file,final Memory memory) {
        this.titleName = "NO NAME";
        this.rom = Loader.load(file).asReadOnlyBuffer();
        length = file.length();
        this.memory = memory;
        fillMemory();
    }

    private void fillMemory() {        
        memory.clear();
        for (int address = 0; address < length ; address++) {
            if (address > ROM_END){
                throw new IllegalStateException("This ROM exceds the max size of Chip16! Max lenght: "+ROM_END+" bytes.");
            }
            memory.writeAt(ROM_START+address, readUnsignedByte(rom));
        }
    }

    @Override
    public ByteBuffer getRom() {
        return rom;
    }

    @Override
    public String getTitleName() {
        return titleName;
    }

    @Override
    public long getLength() {
        return length;
    }
}
