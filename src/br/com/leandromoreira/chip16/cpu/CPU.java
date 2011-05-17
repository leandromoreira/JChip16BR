package br.com.leandromoreira.chip16.cpu;

import br.com.leandromoreira.chip16.gpu.GPU;
import br.com.leandromoreira.chip16.spu.SPU;
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
    private int programCounter;
    private int stackPointer;
    private int[] registers = new int[NUMBER_OF_REGISTERS];
    private boolean[] flags = new boolean[8];
    private final Instruction[] instructions = new Instruction[NUMBER_OF_INSTRUCTIONS];
    private final Memory memory;
    private final GPU gpu;
    private final SPU spu;
    private final Random rnd = new Random();

    public CPU(final Memory memory, final GPU gpu,final SPU spu) {
        this.memory = memory;
        this.gpu = gpu;
        this.spu = spu;
        programCounter = ROM_START;
        stackPointer = STACK_START;
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
    private short getNumber(final short param1,final short param2){
        return (short) ((param1 << 8) | param2);
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
                gpu.drawSprite(getNumber(parameter.getSecondByte(),parameter.getThirdByte()), x, y);
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
        
        instructions[RND] = new DefaultInstruction(new Executor() {

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.getFirstByte1()] = rnd.nextInt(getNumber(parameter.getSecondByte(),parameter.getThirdByte()));
            }
        }); 
        instructions[NOP_FUTURE] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
            }
        });        
        instructions[SND0] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                spu.stop();
            }
        });                
        instructions[SND1] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play500Mhz(getNumber(parameter.getSecondByte(),parameter.getThirdByte()));
            }
        });        
        instructions[SND2] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play1000Mhz(getNumber(parameter.getSecondByte(),parameter.getThirdByte()));
            }
        });        
        instructions[SND3] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play1500Mhz(getNumber(parameter.getSecondByte(),parameter.getThirdByte()));
            }
        });        
        instructions[JMP] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                programCounter = getNumber(parameter.getSecondByte(),parameter.getThirdByte());
            }
        });    
        instructions[JMC] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                if (flags[FLAG.CARRY_BORROW.ordinal()]){
                    programCounter = getNumber(parameter.getSecondByte(),parameter.getThirdByte());
                }                
            }
        });            
        instructions[JMZ] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                if (flags[FLAG.ZERO.ordinal()]){
                    programCounter = getNumber(parameter.getSecondByte(),parameter.getThirdByte());
                }                
            }
        });                    
        instructions[JME] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                if (registers[parameter.getFirstByte1()]==registers[parameter.getFirstByte0()]){
                    programCounter = getNumber(parameter.getSecondByte(),parameter.getThirdByte());
                }
            }
        });                            
        instructions[CALL] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                memory.writeAt(stackPointer, parameter.getSecondByte());
                memory.writeAt(stackPointer+1, parameter.getThirdByte());
                stackPointer += 2;
            }
        });        
        instructions[RET] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                stackPointer -= 2;
                programCounter = getNumber(memory.readFrom(stackPointer),memory.readFrom(stackPointer+1));
            }
        });     
        instructions[LDI_RX] = new DefaultInstruction(new Executor() {
            
            @Override
            public void execute(OpCodeParameter parameter) {
                stackPointer -= 2;
                programCounter = getNumber(memory.readFrom(stackPointer),memory.readFrom(stackPointer+1));
            }
        });        
        /*
20 0X LL HH	LDI RX, HHLL		Load immediate value to register X.
21 00 LL HH	LDI SP, HHLL		Point SP to the specified address. Does not move existing values in memory to new location.
22 0X LL HH	LDM RX, HHLL		Load register X with the 16bit value at the specified address.
23 YX 00 00	LDM RX, RY		Load register X with the 16bit value at the specified address pointed by register Y.
24 YX 00 00	MOV RX, RY		Copy data from register Y to register X.

30 0X LL HH	STM RX, HHLL		Store value of register X at the specified address.
31 YX 00 00	STM RX, RY		Store value of register X at the specified address pointed by register Y.

40 0X LL HH	ADDI RX			Add immediate value to register X. Affects carry flag and zero flag.
41 YX 00 00	ADD RX, RY		Add value of register Y to register X. Result is stored in register X. Affects carry flag and zero flag.
42 YX 0Z 00	ADD RX, RY, RZ		Add value of register Y to register X. Result is stored in register Z. Affects carry flag and zero flag.

50 0X LL HH	SUBI RX			Subtract immediate value from register X. Result is stored in register X. Affects borrow flag and zero flag.
51 YX 00 00	SUB RX, RY		Substract value of register Y from register X. Result is stored in register X. Affects borrow flag and zero flag.
52 YX 0Z 00	SUB RX, RY, RZ		Substract value of register Y from register X. Result is stored in register Z. Affects borrow flag and zero flag.

60 0X LL HH	ANDI RX			AND immediate value with register X. Result is stored in register X. Affects zero flag.
61 YX 00 00	AND RX, RY		AND value of register Y with value of register X. Result is stored in register X. Affects zero flag.
62 YX 0Z 00	AND RX, RY, RZ		AND value of register Y with value of register X. Result is stored in register Z. Affects zero flag.

70 0X LL HH	ORI RX			OR immediate value with register X. Result is stored in register X. Affects zero flag.
71 YX 00 00	OR RX, RY		OR value of register Y with value of register X. Result is stored in register X. Affects zero flag.
72 YX 0Z 00	OR RX, RY, RZ		OR value of register Y with value of register X. Result is stored in register Z. Affects zero flag.

80 0X LL HH	XORI RX			XOR immediate value with register X. Result is stored in register X. Affects zero flag.
81 YX 00 00	XOR RX, RY		XOR value of register Y with value of register X. Result is stored in register X. Affects zero flag.
82 YX 0Z 00	XOR RX, RY, RZ		XOR value of register Y with value of register X. Result is stored in register Z. Affects zero flag.

90 0X LL HH	MULI RX			Multiply immediate value with register X. Result is stored in register X. Affects carry flag and zero flag.
91 YX 00 00	MUL RX, RY		Multiply value of register Y with value of register X. Result is stored in register X. Affects carry flag and zero flag.
92 YX 0Z 00	MUL RX, RY, RZ		Multiply value of register Y with value of register X. Result is stored in register Z. Affects carry flag and zero flag.

A0 0X LL HH	DIVI RX			Divide immediate value with register X. Result is stored in register X. Affects borrow flag and zero flag.
A1 YX 00 00	DIV RX, RY		Divide value of register Y with value of register X. Result is stored in register X. Affects borrow flag and zero flag.
A2 YX 0Z 00	DIV RX, RY, RZ		Divide value of register Y with value of register X. Result is stored in register Z. Affects borrow flag and zero flag.

B0 0X 0N 00	SHL RX, N		Shift value in register X left N times. Affects borrow flag and zero flag.
B1 0X 0N 00	SHR RX, N		Shift value in register X right N times. Affects carry flag and zero flag.
         * 
         * 
         */
        initRegisters();
    }

    private void initRegisters() {
        registers = new int[NUMBER_OF_REGISTERS];
        flags = new boolean[8];
    }
}