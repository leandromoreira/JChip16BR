package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.cpu.CPU;
import br.com.leandromoreira.chip16.cpu.Memory;
import br.com.leandromoreira.chip16.gpu.GPU;
import br.com.leandromoreira.chip16.rom.Chip16ROM;
import br.com.leandromoreira.chip16.util.JavaEmuUtil;
import java.io.File;

/**
 * @author leandro-rm
 */
public class Chip16Machine {
    public static class CPUInfo{
        private final CPU cpu;
        CPUInfo(CPU cpu){
            this.cpu = cpu;
        }
        public String getPC(){return JavaEmuUtil.getHexadecimal4Formatted(cpu.getProgramCounter());}
        public String getSP(){return JavaEmuUtil.getHexadecimal4Formatted(cpu.getStackPointer());}
        public String getR0(){return getRegister(0);}
        public String getR1(){return getRegister(1);}
        public String getR2(){return getRegister(2);}
        public String getR3(){return getRegister(3);}
        public String getR4(){return getRegister(4);}
        public String getR5(){return getRegister(5);}
        public String getR6(){return getRegister(6);}
        public String getR7(){return getRegister(7);}
        public String getR8(){return getRegister(8);}
        public String getR9(){return getRegister(9);}
        public String getRA(){return getRegister(10);}
        public String getRB(){return getRegister(11);}
        public String getRC(){return getRegister(12);}
        public String getRD(){return getRegister(13);}
        public String getRE(){return getRegister(14);}
        public String getRF(){return getRegister(15);}
        private String getRegister(int number){
            return JavaEmuUtil.getHexadecimal4Formatted(cpu.getRegister(number));
        }
    }
    private final CPU cpu;
    private final GPU gpu;
    private final Memory memory;
    private final Chip16ROM rom;
    private final CPUInfo CPUInfo;
    public Chip16Machine(final File romFile){
        memory = new Memory();
        rom = new Chip16ROM(romFile.getName(), romFile, memory);
        gpu = new GPU();
        cpu =  new CPU(memory,gpu);
        CPUInfo = new CPUInfo(cpu);
    }

    public CPUInfo getCPUInfo() {
        return CPUInfo;
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

