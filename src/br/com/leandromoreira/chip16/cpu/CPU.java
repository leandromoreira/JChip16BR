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

    public enum ORDERING {

        LITTLE_ENDIAN, ENDIANESS
    };

    public enum FLAG {

        VBLANK, CARRY_BORROW, ZERO
    };
    private static final ORDERING ordering_type = ORDERING.LITTLE_ENDIAN;
    private static final int NUMBER_OF_REGISTERS = 16;
    private static final int NUMBER_OF_INSTRUCTIONS = 178;
    private int programCounter;
    private int stackPointer;
    private int[] registers = new int[NUMBER_OF_REGISTERS];
    private boolean[] flags = new boolean[8];
    private final Instruction[] instructions = new Instruction[NUMBER_OF_INSTRUCTIONS];
    private final Memory memory;
    private final GPU gpu;
    private short firstByte, secondByte, thirdByte;

    public CPU(final Memory memory, final GPU gpu) {
        this.memory = memory;
        this.gpu = gpu;
        programCounter = ROM_START;
        init();
    }

    private void init() {
        initRegisters();
        instructions[NOP.ordinal()] = new DefaultInstruction(NOP, new Executor() {

            @Override
            public void execute() {
            }
        });
        instructions[CLS.ordinal()] = new DefaultInstruction(CLS, new Executor() {

            @Override
            public void execute() {
                gpu.clear();
            }
        });
        instructions[VBLNK.ordinal()] = new DefaultInstruction(VBLNK, new Executor() {

            @Override
            public void execute() {
                flags[FLAG.VBLANK.ordinal()] = false;
            }
        });
    }

    private void initRegisters() {
    }

    public void step() {
        final short opCode = memory.readFrom(programCounter);
        firstByte = memory.readFrom(programCounter + 1);
        secondByte = memory.readFrom(programCounter + 2);
        thirdByte = memory.readFrom(programCounter + 3);
        programCounter += 3;
        instructions[opCode].execute();
    }

    public int getProgramCounter() {
        return programCounter;
    }
}
