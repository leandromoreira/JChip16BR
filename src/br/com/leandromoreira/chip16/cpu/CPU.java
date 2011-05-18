package br.com.leandromoreira.chip16.cpu;

import br.com.leandromoreira.chip16.gpu.GPU;
import br.com.leandromoreira.chip16.spu.SPU;
import br.com.leandromoreira.chip16.util.JavaEmuUtil;
import java.util.Random;
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
    private static final int BOUND = 0xFFFF;
    private int programCounter;
    private int stackPointer;
    private int[] registers = new int[NUMBER_OF_REGISTERS];
    private boolean[] flags = new boolean[8];
    private final Instruction[] instructions = new Instruction[NUMBER_OF_INSTRUCTIONS];
    private final Memory memory;
    private final GPU gpu;
    private final SPU spu;
    private final Random rnd = new Random();

    public CPU(final Memory memory, final GPU gpu, final SPU spu) {
        this.memory = memory;
        this.gpu = gpu;
        this.spu = spu;
        programCounter = ROM_START;
        stackPointer = STACK_START;
        initInstructionTable();
    }

    public int getRegister(int number) {
        return registers[number];
    }
    
    public void setVBlank(boolean value) {
        flags[FLAG.VBLANK.ordinal()] = value;
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
        programCounter += instructions[opCode].addToPC();
    }

    private void initInstructionTable() {
        instructions[NOP] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
            }
        };
        instructions[CLS] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.clear();
            }
        };
        instructions[VBLNK] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                flags[FLAG.VBLANK.ordinal()] = false;
            }
        };
        instructions[BGC] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.setBackgroundColor(parameter.getSecondByte1());
            }
        };
        instructions[SPR] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.setSprite(parameter.getSecondByte(), parameter.getThirdByte());
            }
        };
        instructions[DRW_HHL] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int x = registers[parameter.getFirstByte1()];
                final int y = registers[parameter.getFirstByte0()];
                gpu.drawSprite(JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte()), x, y);
            }
        };
        instructions[DRW_RZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int x = registers[parameter.getFirstByte1()];
                final int y = registers[parameter.getFirstByte0()];
                final int address = registers[parameter.getSecondByte1()];
                gpu.drawSprite(address, x, y);
            }
        };

        instructions[RND] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] = rnd.nextInt(JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte())+1);
            }
        };
        instructions[NOP_FUTURE] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
            }
        };
        instructions[SND0] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                spu.stop();
            }
        };
        instructions[SND1] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play500Mhz(JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte()));
            }
        };
        instructions[SND2] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play1000Mhz(JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte()));
            }
        };
        instructions[SND3] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play1500Mhz(JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte()));
            }
        };
        instructions[JMP] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                programCounter = JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                setSumToPC(0);
            }
        };
        
        instructions[JMC] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                if (flags[FLAG.CARRY_BORROW.ordinal()]) {
                    programCounter = JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                    setSumToPC(0);
                }else{
                    setSumToPC(4);
                }
            }
        };
        instructions[JMZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                if (flags[FLAG.ZERO.ordinal()]) {
                    programCounter = JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                    setSumToPC(0);
                }else{
                    setSumToPC(4);
                }
            }
        };
        instructions[JME] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                if (registers[parameter.getFirstByte1()] == registers[parameter.getFirstByte0()]) {
                    programCounter = JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                    setSumToPC(0);
                }else{
                    setSumToPC(4);
                }
            }
        };
        instructions[CALL] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                memory.writeAt(stackPointer, (short) (programCounter & 0xFF));
                memory.writeAt(stackPointer + 1, (short) (programCounter >> 8));
                stackPointer += 2;
                programCounter = JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                setSumToPC(0);
            }
        };
        instructions[RET] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                stackPointer -= 2;
                programCounter = JavaEmuUtil.getLittleEndian(memory.readFrom(stackPointer), memory.readFrom(stackPointer + 1));
            }
        };
        instructions[LDI_RX] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] = JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
            }
        };
        instructions[LDI_SP] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                stackPointer = JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
            }
        };
        instructions[LDM_HHLL] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] = memory.readFrom(JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte()));
            }
        };
        instructions[LDM_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] = memory.readFrom(registers[parameter.getFirstByte0()]);
            }
        };
        instructions[MOV] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] = registers[parameter.getFirstByte0()];
            }
        };
        instructions[STM_HHLL] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                memory.writeAt(JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte()), (short) (registers[parameter.getFirstByte1()] & 0xF));
                memory.writeAt(JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte()) + 1, (short) (registers[parameter.getFirstByte1()] >> 8));
            }
        };
        instructions[STM_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                memory.writeAt(registers[parameter.getFirstByte0()], (short) (registers[parameter.getFirstByte1()] & 0xF));
                memory.writeAt(registers[parameter.getFirstByte0()] + 1, (short) (registers[parameter.getFirstByte1()] >> 8));
            }
        };
        instructions[ADDI] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] + JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                registers[parameter.getFirstByte1()] = result & BOUND;
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result>BOUND){
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[ADD_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] + registers[parameter.getFirstByte0()];
                registers[parameter.getFirstByte1()] = result & BOUND;
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result>BOUND){
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };        
        instructions[ADD_RZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] + registers[parameter.getFirstByte0()];
                registers[parameter.getSecondByte1()] = result & BOUND;
                if (registers[parameter.getSecondByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result>BOUND){
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[SUBI] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] - JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                registers[parameter.getFirstByte1()] = result & BOUND;
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result < 0) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }                
                
            }
        };
        instructions[SUB_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] - registers[parameter.getFirstByte0()];
                registers[parameter.getFirstByte1()] = result & BOUND;
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result < 0) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }  
            }
        };        
        instructions[SUB_RZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] - registers[parameter.getFirstByte0()];
                registers[parameter.getSecondByte1()] = result & BOUND;
                if (registers[parameter.getSecondByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result < 0) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }                  
            }
        };
        instructions[ANDI] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] &= JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[AND_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] &= registers[parameter.getFirstByte0()];
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };        
        instructions[AND_RZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getSecondByte1()] = registers[parameter.getFirstByte1()] & registers[parameter.getFirstByte0()];
                if (registers[parameter.getSecondByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        }; 
        instructions[ORI] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] |= JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[OR_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] |= registers[parameter.getFirstByte0()];
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };        
        instructions[OR_RZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getSecondByte1()] = registers[parameter.getFirstByte1()] | registers[parameter.getFirstByte0()];
                if (registers[parameter.getSecondByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[XORI] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] ^= JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[XOR_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] ^= registers[parameter.getFirstByte0()];
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };        
        instructions[XOR_RZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getSecondByte1()] = registers[parameter.getFirstByte1()] ^ registers[parameter.getFirstByte0()];
                if (registers[parameter.getSecondByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[MULI] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] * JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                registers[parameter.getFirstByte1()] = result & BOUND;
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }                
                
            }
        };
        instructions[MUL_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] * registers[parameter.getFirstByte0()];
                registers[parameter.getFirstByte1()] = result & BOUND ;
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result>BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }  
            }
        };        
        instructions[MUL_RZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] * registers[parameter.getFirstByte0()];
                registers[parameter.getSecondByte1()] = result & BOUND;
                if (registers[parameter.getSecondByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }         
            }
        };        
        instructions[DIVI] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final boolean thereIsMod = registers[parameter.getFirstByte1()] % JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte()) > 1;
                registers[parameter.getFirstByte1()] /= JavaEmuUtil.getLittleEndian(parameter.getSecondByte(), parameter.getThirdByte());
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (thereIsMod) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }                
                
            }
        };
        instructions[DIV_RY] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final boolean thereIsMod = registers[parameter.getFirstByte1()] % registers[parameter.getFirstByte0()] > 1;
                registers[parameter.getFirstByte1()] /= registers[parameter.getFirstByte0()];
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (thereIsMod) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }  
            }
        };        
        instructions[DIV_RZ] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final boolean thereIsMod = registers[parameter.getFirstByte1()] % registers[parameter.getFirstByte0()] > 1;
                registers[parameter.getSecondByte1()] = registers[parameter.getFirstByte1()] / registers[parameter.getFirstByte0()];
                if (registers[parameter.getSecondByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (thereIsMod) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }                  
            }
        };        
        instructions[SHL] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] << parameter.getSecondByte1();
                registers[parameter.getFirstByte1()] = result & BOUND;
                
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result>BOUND){
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };        
        instructions[SHR] = new DefaultInstruction() {

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.getFirstByte1()] >> parameter.getSecondByte1();
                registers[parameter.getFirstByte1()] = result & BOUND;
                
                if (registers[parameter.getFirstByte1()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                }else{
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result<0){
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                }else{
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };        
    }
}