package br.com.leandromoreira.chip16.cpu;

import static br.com.leandromoreira.chip16.cpu.OpCode.*;

/**
 * @author leandro-rm
 */
public class CPU {
    public enum REGISTER{R0,R1,R2,R3,R4,R5,R6,R7,R8,R9,RA,RB,RC,RD,RE,RF};
    public enum ORDERING{LITTLE_ENDIAN,ENDIANESS};
    public enum FLAG{VBLANK,CARRY_BORROW,ZERO};
    private static final ORDERING ordering_type = ORDERING.LITTLE_ENDIAN;
    private static final int NUMBER_OF_REGISTERS = 16;
    private static final int NUMBER_OF_INSTRUCTIONS = 178;
    private int programCounter;
    private int stackPointer;
    private int[] registers = new int[NUMBER_OF_REGISTERS];
    private boolean[] flags = new boolean[8];
    private final Instruction[] instructions = new Instruction[NUMBER_OF_INSTRUCTIONS];
    
    private void init(){
        initRegisters();
        instructions[NOP.ordinal()] = new Instruction() {
            @Override
            public void execute() {
            }
            @Override
            public String getAssembler() {
                return NOP.name();
            }
        };
    }
    private void initRegisters() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
        
    public void step(){
        int opCode = getOpCode();
        instructions[opCode].execute();
    }
    
    private int getOpCode() {
        throw new UnsupportedOperationException("Not yet implemented");
    }
    
}
