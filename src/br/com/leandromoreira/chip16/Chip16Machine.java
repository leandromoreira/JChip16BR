package br.com.leandromoreira.chip16;

import br.com.leandromoreira.chip16.cpu.CPU;
import br.com.leandromoreira.chip16.cpu.Chip16CPU;
import br.com.leandromoreira.chip16.cpu.Memory;
import br.com.leandromoreira.chip16.cpu.MemoryMap;
import br.com.leandromoreira.chip16.cpu.OpCode;
import br.com.leandromoreira.chip16.gpu.Colors;
import br.com.leandromoreira.chip16.gpu.GPU;
import br.com.leandromoreira.chip16.gpu.GPUFrameBuffer;
import br.com.leandromoreira.chip16.gpu.Render;
import br.com.leandromoreira.chip16.rom.Chip16ROM;
import br.com.leandromoreira.chip16.rom.ROM;
import br.com.leandromoreira.chip16.spu.SPU;
import br.com.leandromoreira.chip16.spu.SPUJavaSound;
import br.com.leandromoreira.chip16.util.JavaEmuUtil;
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

        private String getRegister(final int number) {
            return JavaEmuUtil.getHexadecimal4Formatted(cpu.getRegister(number));
        }
    }

    public static class GPUInfo {

        private final GPU gpu;

        public GPUInfo(final GPU gpu) {
            this.gpu = gpu;
        }

        public String getBGColor() {
            return (gpu.getBackgroundColor() == 0) ? "" : Colors.getColor(0).toString();
        }

        public String getCurrentSprite() {
            return (gpu.getCurrentSprite() == null) ? "" : gpu.getCurrentSprite().toString();
        }

        public String getSpriteAddress() {
            return (gpu.getSpriteAddress() == 0) ? "" : " at " + JavaEmuUtil.getHexadecimal4Formatted(gpu.getSpriteAddress());
        }
    }
    private final CPU cpu;
    private final GPU gpu;
    private final SPU spu;
    private final Memory memory;
    private final ROM rom;
    private final CPUInfo CPUInfo;
    private final GPUInfo GPUInfo;
    private Thread currentMachineInstance;
    private Chip16MainLoop currentLoop;

    public Chip16Machine(final File romFile) {
        memory = new Memory();
        rom = new Chip16ROM(romFile.getName(), romFile, memory);
        gpu = new GPUFrameBuffer(memory);
        spu = new SPUJavaSound();
        cpu = new Chip16CPU(memory, gpu, spu);
        CPUInfo = new CPUInfo(cpu);
        GPUInfo = new GPUInfo(gpu);
    }

    public void start(Render render) {
        if (currentLoop != null) {
            stop();
        }
        currentLoop = new Chip16MainLoop(this, render);
        currentMachineInstance = new Thread(currentLoop);
        currentMachineInstance.start();
    }

    public void stop() {
        currentLoop.stop();
        try {
            Thread.sleep(26);
        } catch (InterruptedException ex) {
            System.exit(0);
        }
    }

    public void debugStep(final Render render) {
        resetVBlank();
        cpuStep();
        drawFrame(render);
        raiseVBlank();
    }

    public void drawFrame(final Render render) {
        gpu.drawFrame(render);
    }

    public void raiseVBlank() {
        cpu.setFlag(Chip16CPU.FLAG.VBLANK.ordinal(), true);
    }

    public void resetVBlank() {
        cpu.setFlag(Chip16CPU.FLAG.VBLANK.ordinal(), false);
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

    public ROM getRom() {
        return rom;
    }

    public String getFormattedStack() {
        final StringBuilder sb = new StringBuilder();
        final short[] memoryCopy = getMemory().getMemoryCopy();
        for (int i = 1; i <= 32; i++) {
            sb.append((i <= 9 ? "0" + i : i)).append(": ").append(JavaEmuUtil.getHexadecimal2Formatted(memoryCopy[MemoryMap.STACK_START + i - 1])).append("\n");
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

    public static class Assembler {

        private final String line;
        private final int pc;

        public Assembler(final String line, final int pc) {
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

    public List<Assembler> getAssembler(final int start) {
        final List<Assembler> values = new LinkedList<Assembler>();
        for (int i = start; i < rom.getLength();) {
            final int opCodeAddress = i;
            final short opCode = getMemory().readFrom(i++);
            final short firstByte = getMemory().readFrom(i++);
            final short secondByte = getMemory().readFrom(i++);
            final short thirdByte = getMemory().readFrom(i++);
            try {
                values.add(new Assembler(OpCode.assembler(opCode, firstByte, secondByte, thirdByte), opCodeAddress));
            } catch (IllegalArgumentException e) {
                values.add(new Assembler(JavaEmuUtil.getHexadecimal2Formatted(opCodeAddress), opCodeAddress));
                i = opCodeAddress + 1;
            }
        }
        return values;
    }
}
