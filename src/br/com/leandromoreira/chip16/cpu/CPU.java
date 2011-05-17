package br.com.leandromoreira.chip16.cpu;

import br.com.leandromoreira.chip16.gpu.GPU;
import static br.com.leandromoreira.chip16.cpu.MemoryMap.*;
import static br.com.leandromoreira.chip16.cpu.OpCode.*;

/**
 * @author leandro-rm
 */
public class CPU {

    public enum REGISTER {

        R0, R1, R2, R3, R4, R5, R6, R7, R8, R9, RA, RB, RC, RD, RE, RF
    };

    public enum FLAG {

        VBLANK, CARRY_BORROW, ZERO
    };
    private static final int NUMBER_OF_REGISTERS = 16;
    private static final int NUMBER_OF_INSTRUCTIONS = 178;
    private int programCounter;
    private int stackPointer;
    private int[] registers = new int[NUMBER_OF_REGISTERS];
    private boolean[] flags = new boolean[8];
    private final Instruction[] instructions = new Instruction[NUMBER_OF_INSTRUCTIONS];
    private final Memory memory;
    private final GPU gpu;

    public CPU(final Memory memory, final GPU gpu) {
        this.memory = memory;
        this.gpu = gpu;
        programCounter = ROM_START;
        init();
    }

    public int getRegister(int number) {
        return registers[number];
    }

    public boolean getFlag(int number) {
        return flags[number];
    }

    public int getStackPointer() {
        return stackPointer;
    }

    public int getProgramCounter() {
        return programCounter;
    }

    public void step() {
        final short opCode = memory.readFrom(programCounter);
        instructions[opCode].execute(new OpCodeParameter(memory.readFrom(programCounter + 1), memory.readFrom(programCounter + 2), memory.readFrom(programCounter + 3)));
        programCounter += 4;
    }

    private void init() {
        instructions[NOP] = new DefaultInstruction(new Executor() {

            @Override
            public void execute(OpCodeParameter parameter) {
            }
        });
        instructions[CLS] = new DefaultInstruction(new Executor() {

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.clear();
            }
        });
        instructions[VBLNK] = new DefaultInstruction(new Executor() {

            @Override
            public void execute(OpCodeParameter parameter) {
                flags[FLAG.VBLANK.ordinal()] = false;
            }
        });
        instructions[BGC] = new DefaultInstruction(new Executor() {

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.setBackgroundColor(parameter.getSecondByte1());
            }
        });
        instructions[SPR] = new DefaultInstruction(new Executor() {

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.setSprite(parameter.getSecondByte(), parameter.getThirdByte());
            }
        });
        instructions[DRW_HHL] = new DefaultInstruction(new Executor() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int x = registers[parameter.getFirstByte1()];
                final int y = registers[parameter.getFirstByte0()];
                gpu.drawSprite((parameter.getSecondByte() << 8) | parameter.getThirdByte(), x, y);
            }
        });
        instructions[DRW_RZ] = new DefaultInstruction(new Executor() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int x = registers[parameter.getFirstByte1()];
                final int y = registers[parameter.getFirstByte0()];
                final int address = registers[parameter.getSecondByte1()];
                gpu.drawSprite(memory.readFrom(address), x, y);
            }
        });
        initRegisters();
    }

    private void initRegisters() {
        registers = new int[NUMBER_OF_REGISTERS];
        flags = new boolean[8];
    }
}