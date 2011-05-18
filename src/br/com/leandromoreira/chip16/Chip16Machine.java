package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.cpu.CPU;
import br.com.leandromoreira.chip16.cpu.Memory;
import br.com.leandromoreira.chip16.cpu.MemoryMap;
import br.com.leandromoreira.chip16.cpu.OpCode;
import br.com.leandromoreira.chip16.gpu.Color;
import br.com.leandromoreira.chip16.gpu.GPU;
import br.com.leandromoreira.chip16.rom.Chip16ROM;
import br.com.leandromoreira.chip16.spu.SPU;
import br.com.leandromoreira.chip16.util.JavaEmuUtil;
import java.awt.Graphics;
import java.io.File;
import java.util.LinkedList;
import java.util.List;

/**
 * @author leandro-rm
 */
public class Chip16Machine {


    public static class CPUInfo {

        private final CPU cpu;

        CPUInfo(CPU cpu) {
            this.cpu = cpu;
        }

        public String getPC() {
            return JavaEmuUtil.getHexadecimal4Formatted(cpu.getProgramCounter()).substring(2);
        }

        public String getSP() {
            return JavaEmuUtil.getHexadecimal4Formatted(cpu.getStackPointer());
        }

        public boolean getFlagV() {
            return cpu.getFlag(0);
        }

        public boolean getFlagC() {
            return cpu.getFlag(1);
        }

        public boolean getFlagZ() {
            return cpu.getFlag(2);
        }

        public String getR0() {
            return getRegister(0);
        }

        public String getR1() {
            return getRegister(1);
        }

        public String getR2() {
            return getRegister(2);
        }

        public String getR3() {
            return getRegister(3);
        }

        public String getR4() {
            return getRegister(4);
        }

        public String getR5() {
            return getRegister(5);
        }

        public String getR6() {
            return getRegister(6);
        }

        public String getR7() {
            return getRegister(7);
        }

        public String getR8() {
            return getRegister(8);
        }

        public String getR9() {
            return getRegister(9);
        }

        public String getRA() {
            return getRegister(10);
        }

        public String getRB() {
            return getRegister(11);
        }

        public String getRC() {
            return getRegister(12);
        }

        public String getRD() {
            return getRegister(13);
        }

        public String getRE() {
            return getRegister(14);
        }

        public String getRF() {
            return getRegister(15);
        }

        private String getRegister(int number) {
            return JavaEmuUtil.getHexadecimal4Formatted(cpu.getRegister(number));
        }
    }
    public static class GPUInfo{
        private final GPU gpu;
        public GPUInfo(GPU gpu) {
            this.gpu = gpu;
        }
        public String getBGColor(){
            return (gpu.getBackgroundColor()==null)?"":gpu.getBackgroundColor().getDescription();
        }
        public String getCurrentSprite(){
            return (gpu.getCurrentSprite()==null)?"":gpu.getCurrentSprite().toString();
        }
        public String getSpriteAddress(){
            return (gpu.getSpriteAddress()==0)?"":" at "+JavaEmuUtil.getHexadecimal4Formatted(gpu.getSpriteAddress());
        }
    }
    private final CPU cpu;
    private final GPU gpu;
    private final SPU spu;
    private final Memory memory;
    private final Chip16ROM rom;
    private final CPUInfo CPUInfo;
    private final GPUInfo GPUInfo;

    public Chip16Machine(final File romFile,final Graphics graphics) {
        memory = new Memory();
        rom = new Chip16ROM(romFile.getName(), romFile, memory);
        gpu = new GPU(memory);
        spu = new SPU() {
            @Override
            public void play500Mhz(int ms) {
                System.out.println("it was suposse to play 500Mhz for "+ms+"ms");
            }

            @Override
            public void play1000Mhz(int ms) {
                System.out.println("it was suposse to play 1000Mhz for "+ms+"ms");
            }

            @Override
            public void play1500Mhz(int ms) {
                System.out.println("it was suposse to play 1500Mhz for "+ms+"ms");
            }

            @Override
            public void stop() {
                System.out.println("the sound system halts now!");
            }
        };
        cpu = new CPU(memory, gpu,spu);
        CPUInfo = new CPUInfo(cpu);
        GPUInfo = new GPUInfo(gpu);
    }
    
    public void debugStep(final Graphics graphics) {
        cpuStep();
        drawFrame(graphics);
        raiseVBlank();
    }
    
    
    private void drawFrame(final Graphics graphics) {
        Color[][] screen = gpu.getScreen();
        for (int x = 0 ; x < GPU.HEIGHT ; x++){
            for (int y = 0; y < GPU.WIDTH ; y++){
                if (screen[x][y]!=null){
                    graphics.setColor(wrapColor(screen[x][y]));
                    graphics.drawLine(x, y, x, y);
                }else{
                    graphics.setColor(wrapColor(gpu.getBackgroundColor()));
                    graphics.drawLine(x, y, x, y);                
                }
            }
        }
    }

    private java.awt.Color wrapColor(final Color color) {
        if (color==null) return java.awt.Color.BLACK;
        return new  java.awt.Color(color.getR(), color.getG(), color.getB());
    }

    
    public void raiseVBlank() {
        cpu.setVBlank(true);
    }

    public void cpuStep() {
        cpu.step();
    }
    
    public CPUInfo getCPUInfo() {
        return CPUInfo;
    }

    public GPUInfo getGPUInfo() {
        return GPUInfo;
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
    public String getFormattedStack(){
        final StringBuilder sb = new StringBuilder();
        final short[] memoryCopy = getMemory().getMemoryCopy();
        for (int i = 1; i <= 32; i++) {
            sb.append((i<=9?"0"+i:i))
                    .append(": ")
                    .append(JavaEmuUtil.getHexadecimal4Formatted(memoryCopy[MemoryMap.STACK_START+i-1]))
                    .append("\n");
        }
        return sb.toString();
    }
    public String getFormattedMemory() {
        final StringBuilder sb = new StringBuilder();
        final short[] memoryCopy = getMemory().getMemoryCopy();
        for (int i = 0; i < memoryCopy.length - 1;) {
            sb.append(JavaEmuUtil.getHexadecimal4Formatted(i)).append(": ").append(JavaEmuUtil.getHexadecimal2Formatted(memoryCopy[i])).append(" ").append(JavaEmuUtil.getHexadecimal2Formatted(memoryCopy[i + 1])).append(" ").append(JavaEmuUtil.getHexadecimal2Formatted(memoryCopy[i + 2])).append(" ").append(JavaEmuUtil.getHexadecimal2Formatted(memoryCopy[i + 3])).append("\n");
            i += 4;
        }
        return sb.toString();
    }
    public static class Assembler{
        private final String line;
        private final int pc;
        public Assembler(String line, int pc) {
            this.line = line;
            this.pc = pc;
        }
        public String getLine() {
            return line;
        }
        public int getPc() {
            return pc;
        }
    }
    public List<Assembler> getAssembler(){
        final List<Assembler> values = new LinkedList<Assembler>();
        for (int i = 0; i < rom.getLength();) {
            short opCodeAddress = (short) i;
            short opCode = getMemory().readFrom(i++);
            short firstByte = getMemory().readFrom(i++);
            short secondByte = getMemory().readFrom(i++);
            short thirdByte = getMemory().readFrom(i++);
            values.add(new Assembler(OpCode.assembler(opCode,firstByte,secondByte,thirdByte), opCodeAddress) );
        }
        return values;
    }
}
