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
public class Chip16CPUNotOO implements CPU {

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

    public Chip16CPUNotOO(final Memory memory, final GPU gpu, final SPU spu) {
        this.memory = memory;
        this.gpu = gpu;
        this.spu = spu;
        programCounter = ROM_START;
        stackPointer = STACK_START;
        initInstructionTable();
    }

    @Override
    public int getRegister(final int number) {
        return registers[number];
    }

    @Override
    public boolean getFlag(final int number) {
        return flags[number];
    }

    @Override
    public void setFlag(final int number, final boolean value) {
        flags[number] = value;
    }

    @Override
    public int getStackPointer() {
        return stackPointer;
    }

    @Override
    public int getProgramCounter() {
        return programCounter;
    }
    private short opCode, ll, hh, y, x, nOrZ;
    private int hhll;

    @Override
    public void step() {
        opCode = memory.readFrom(programCounter);
        ll = memory.readFrom(programCounter + 2);
        hh = memory.readFrom(programCounter + 3);
        y = (short) (memory.readFrom(programCounter + 1) >> 4);
        x = (short) (memory.readFrom(programCounter + 1) & 0xF);
        nOrZ = (short) (ll & 0xF);
        hhll = ((hh << 8) | ll);
        switch (opCode) {
            case NOP:
                programCounter += 4;
                break;
            case CLS:
                gpu.clear();
                programCounter += 4;
                break;
            case VBLNK:
                if (flags[FLAG.VBLANK.ordinal()]) {
                    programCounter += 4;
                } else {
                    programCounter = 0;
                }
                break;
            case BGC:
                gpu.setBackgroundColor(nOrZ);
                programCounter += 4;
                break;
            case SPR:
                gpu.setSprite(ll, hh);
                programCounter += 4;
                break;
            case DRW_HHL:
                flags[FLAG.CARRY_BORROW.ordinal()] = gpu.drawSprite(hhll, x, y);
                programCounter += 4;
                break;
            case DRW_RZ:
                flags[FLAG.CARRY_BORROW.ordinal()] = gpu.drawSprite(nOrZ, x, y);
                programCounter += 4;
                break;
            /*case RND:
            return "RND R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case NOP_FUTURE:
            return "NOP";
            case SND0:
            return "SND0";
            case SND1:
            return "SND1 " + JavaEmuUtil.getLittleEndian(secondByte, thirdByte);
            case SND2:
            return "SND2 " + JavaEmuUtil.getLittleEndian(secondByte, thirdByte);
            case SND3:
            return "SND3 " + JavaEmuUtil.getLittleEndian(secondByte, thirdByte);
            case JMP:
            return "JMP #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case JMC:
            return "JMC #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case JMZ:
            return "JMZ #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case JME:
            return "JME R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y()) + " , #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case CALL:
            return "CALL #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case RET:
            return "RET";
            case LDI_RX:
            return "LDI R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case LDI_SP:
            return "LDI SP, #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case LDM_HHLL:
            return "LDM R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case LDM_RY:
            return "LDM R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case MOV:
            return "MOV R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case STM_HHLL:
            return "STM R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case STM_RY:
            return "STM R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case ADDI:
            return "ADDI R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case ADD_RY:
            return "ADD R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case ADD_RZ:
            return "ADD R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y()) + ", R" + hexa(parameter.N_Z());
            case SUBI:
            return "SUBI R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case SUB_RY:
            return "SUB R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case SUB_RZ:
            return "SUB R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y()) + ", R" + hexa(parameter.N_Z());
            case ANDI:
            return "ANDI R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case AND_RY:
            return "AND R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case AND_RZ:
            return "AND R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y()) + ", R" + hexa(parameter.N_Z());
            case ORI:
            return "ORI R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case OR_RY:
            return "OR R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case OR_RZ:
            return "OR R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y()) + ", R" + hexa(parameter.N_Z());
            case XORI:
            return "XORI R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case XOR_RY:
            return "XOR R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case XOR_RZ:
            return "XOR R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y()) + ", R" + hexa(parameter.N_Z());
            case MULI:
            return "MULI R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case MUL_RY:
            return "MUL R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case MUL_RZ:
            return "MUL R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y()) + ", R" + hexa(parameter.N_Z());
            case DIVI:
            return "DIVI R" + hexa(parameter.X()) + ", #" + (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case DIV_RY:
            return "DIV R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y());
            case DIV_RZ:
            return "DIV R" + hexa(parameter.X()) + ", R" + hexa(parameter.Y()) + ", R" + hexa(parameter.N_Z());
            case SHL:
            return "SHL R" + hexa(parameter.X()) + ", " + parameter.N_Z();
            case SHR:
            return "SHR R" + hexa(parameter.X()) + ", " + parameter.N_Z();*/
            default:
                throw new IllegalArgumentException("Unkwown opcode!");
        }
    }

    private void initInstructionTable() {
        instructions[NOP] = new DefaultInstruction() {
            //NOP			Not operation. Wastes a cycle.

            @Override
            public void execute(OpCodeParameter parameter) {
            }
        };
        instructions[CLS] = new DefaultInstruction() {
            //CLS			Clear screen (Foreground layer is erased, background is set to index 0).

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.clear();
            }
        };
        instructions[VBLNK] = new DefaultInstruction() {
            //VBLNK			Wait VBlank. Set VBlank flag to 0 and wait untill it becomes 1.

            @Override
            public void execute(OpCodeParameter parameter) {
                flags[FLAG.VBLANK.ordinal()] = false;
            }
        };
        instructions[BGC] = new DefaultInstruction() {
            //BGC N			Set background color to index N. If the index was set to 0 the color is black.

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.setBackgroundColor(parameter.N_Z());
            }
        };
        instructions[SPR] = new DefaultInstruction() {
            //SPR HHLL		Set sprite width (LL) and height (HH).

            @Override
            public void execute(OpCodeParameter parameter) {
                gpu.setSprite(parameter.LL(), parameter.HH());
            }
        };
        instructions[DRW_HHL] = new DefaultInstruction() {
            //DRW RX, RY, HHLL	Draw sprite from address HHLL at coordinates stored in register X and Y. Affects carry flag (explained in GPU info).

            @Override
            public void execute(OpCodeParameter parameter) {
                final int x = registers[parameter.X()];
                final int y = registers[parameter.Y()];
                flags[FLAG.CARRY_BORROW.ordinal()] = gpu.drawSprite(parameter.HHLL(), x, y);
            }
        };
        instructions[DRW_RZ] = new DefaultInstruction() {
            //DRW RX, RY, RZ		Draw sprite from address pointed by register Z at coordinates stored in register X and Y.  Affects carry flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int x = registers[parameter.X()];
                final int y = registers[parameter.Y()];
                final int address = registers[parameter.N_Z()];
                flags[FLAG.CARRY_BORROW.ordinal()] = gpu.drawSprite(address, x, y);
            }
        };

        instructions[RND] = new DefaultInstruction() {
            //RND RX, HHLL		Generate a random number and store it in register X. Maximum value is HHLL.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] = rnd.nextInt(parameter.HHLL() + 1);
            }
        };
        instructions[NOP_FUTURE] = new DefaultInstruction() {
            //NOP			Reserved for future updates.

            @Override
            public void execute(OpCodeParameter parameter) {
            }
        };
        instructions[SND0] = new DefaultInstruction() {
            //SND0			Stop playing sounds.

            @Override
            public void execute(OpCodeParameter parameter) {
                spu.stop();
            }
        };
        instructions[SND1] = new DefaultInstruction() {
            //SND1 HHLL		Play 500Hz tone for HHLL miliseconds.

            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play500Mhz(parameter.HHLL());
            }
        };
        instructions[SND2] = new DefaultInstruction() {
            //SND2 HHLL		Play 1000Hz tone for HHLL miliseconds.

            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play1000Mhz(parameter.HHLL());
            }
        };
        instructions[SND3] = new DefaultInstruction() {
            //SND3 HHLL		Play 1500Hz tone for HHLL miliseconds.

            @Override
            public void execute(OpCodeParameter parameter) {
                spu.play1500Mhz(parameter.HHLL());
            }
        };
        instructions[JMP] = new DefaultInstruction() {
            //JMP HHLL		Jump to the specified address.

            @Override
            public void execute(OpCodeParameter parameter) {
                programCounter = parameter.HHLL();
                setSumToPC(0);
            }
        };

        instructions[JMC] = new DefaultInstruction() {
            //JMC HHLL		Jump to the specified address if carry flag is raised.

            @Override
            public void execute(OpCodeParameter parameter) {
                if (flags[FLAG.CARRY_BORROW.ordinal()]) {
                    programCounter = parameter.HHLL();
                    setSumToPC(0);
                } else {
                    setSumToPC(4);
                }
            }
        };
        instructions[JMZ] = new DefaultInstruction() {
            //JMZ HHLL		Jump to the specified address if zero flag is raised.

            @Override
            public void execute(OpCodeParameter parameter) {
                if (flags[FLAG.ZERO.ordinal()]) {
                    programCounter = parameter.HHLL();
                    setSumToPC(0);
                } else {
                    setSumToPC(4);
                }
            }
        };
        instructions[JME] = new DefaultInstruction() {
            //JME RX, RY, HHLL	Jump to the specified address if value in register X is equal to value in register Y.

            @Override
            public void execute(OpCodeParameter parameter) {
                if (registers[parameter.X()] == registers[parameter.Y()]) {
                    programCounter = parameter.HHLL();
                    setSumToPC(0);
                } else {
                    setSumToPC(4);
                }
            }
        };
        instructions[CALL] = new DefaultInstruction() {
            //CALL HHLL		Call subroutine at the specified address. Store PC to stack beforehand. Increase SP by 2.

            @Override
            public void execute(OpCodeParameter parameter) {
                programCounter += 4;
                memory.writeAt(stackPointer, (short) (programCounter & 0xFF));
                memory.writeAt(stackPointer + 1, (short) (programCounter >> 8));
                stackPointer += 2;
                programCounter = parameter.HHLL();
                setSumToPC(0);
            }
        };
        instructions[RET] = new DefaultInstruction() {
            //RET			Return from a subroutine. Get PC from stack. Decrease SP by 2.

            @Override
            public void execute(OpCodeParameter parameter) {
                stackPointer -= 2;
                programCounter = JavaEmuUtil.getLittleEndian(memory.readFrom(stackPointer), memory.readFrom(stackPointer + 1));
                setSumToPC(0);
            }
        };
        instructions[LDI_RX] = new DefaultInstruction() {
            //LDI RX, HHLL		Load immediate value to register X.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] = parameter.HHLL();
            }
        };
        instructions[LDI_SP] = new DefaultInstruction() {
            //LDI SP, HHLL		Point SP to the specified address. Does not move existing values in memory to new location.

            @Override
            public void execute(OpCodeParameter parameter) {
                stackPointer = parameter.HHLL();
            }
        };
        instructions[LDM_HHLL] = new DefaultInstruction() {
            //LDM RX, HHLL		Load register X with the 16bit value at the specified address.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] = JavaEmuUtil.getLittleEndian(memory.readFrom(parameter.HHLL()), memory.readFrom(parameter.HHLL() + 1));
            }
        };
        instructions[LDM_RY] = new DefaultInstruction() {
            //LDM RX, RY		Load register X with the 16bit value at the specified address pointed by register Y.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] = JavaEmuUtil.getLittleEndian(memory.readFrom(registers[parameter.Y()]), memory.readFrom(registers[parameter.Y()] + 1));
            }
        };
        instructions[MOV] = new DefaultInstruction() {
            //MOV RX, RY		Copy data from register Y to register X.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] = registers[parameter.Y()];
            }
        };
        instructions[STM_HHLL] = new DefaultInstruction() {
            //STM RX, HHLL		Store value of register X at the specified address.

            @Override
            public void execute(OpCodeParameter parameter) {
                memory.writeAt(parameter.HHLL(), (short) (registers[parameter.X()] & 0xFF));
                memory.writeAt(parameter.HHLL() + 1, (short) (registers[parameter.X()] >> 8));
            }
        };
        instructions[STM_RY] = new DefaultInstruction() {
            //STM RX, RY		Store value of register X at the specified address pointed by register Y.

            @Override
            public void execute(OpCodeParameter parameter) {
                memory.writeAt(registers[parameter.Y()], (short) (registers[parameter.X()] & 0xFF));
                memory.writeAt(registers[parameter.Y()] + 1, (short) (registers[parameter.X()] >> 8));
            }
        };
        instructions[ADDI] = new DefaultInstruction() {
            //ADDI RX			Add immediate value to register X. Affects carry flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] + parameter.HHLL();
                registers[parameter.X()] = result & BOUND;
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[ADD_RY] = new DefaultInstruction() {
            //ADD RX, RY		Add value of register Y to register X. Result is stored in register X. Affects carry flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] + registers[parameter.Y()];
                registers[parameter.X()] = result & BOUND;
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[ADD_RZ] = new DefaultInstruction() {
            //ADD RX, RY, RZ		Add value of register Y to register X. Result is stored in register Z. Affects carry flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] + registers[parameter.Y()];
                registers[parameter.N_Z()] = result & BOUND;
                if (registers[parameter.N_Z()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[SUBI] = new DefaultInstruction() {
            //SUBI RX			Subtract immediate value from register X. Result is stored in register X. Affects borrow flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] - parameter.HHLL();
                registers[parameter.X()] = result & BOUND;
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result < 0) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }

            }
        };
        instructions[SUB_RY] = new DefaultInstruction() {
            //SUB RX, RY		Substract value of register Y from register X. Result is stored in register X. Affects borrow flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] - registers[parameter.Y()];
                registers[parameter.X()] = result & BOUND;
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result < 0) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[SUB_RZ] = new DefaultInstruction() {
            //SUB RX, RY, RZ		Substract value of register Y from register X. Result is stored in register Z. Affects borrow flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] - registers[parameter.Y()];
                registers[parameter.N_Z()] = result & BOUND;
                if (registers[parameter.N_Z()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result < 0) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[ANDI] = new DefaultInstruction() {
            //ANDI RX			AND immediate value with register X. Result is stored in register X. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] &= parameter.HHLL();
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[AND_RY] = new DefaultInstruction() {
            //AND RX, RY		AND value of register Y with value of register X. Result is stored in register X. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] &= registers[parameter.Y()];
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[AND_RZ] = new DefaultInstruction() {
            //AND RX, RY, RZ		AND value of register Y with value of register X. Result is stored in register Z. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.N_Z()] = registers[parameter.X()] & registers[parameter.Y()];
                if (registers[parameter.N_Z()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[ORI] = new DefaultInstruction() {
            //ORI RX			OR immediate value with register X. Result is stored in register X. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] |= parameter.HHLL();
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[OR_RY] = new DefaultInstruction() {
            //OR RX, RY		OR value of register Y with value of register X. Result is stored in register X. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] |= registers[parameter.Y()];
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[OR_RZ] = new DefaultInstruction() {
            //OR RX, RY, RZ		OR value of register Y with value of register X. Result is stored in register Z. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.N_Z()] = registers[parameter.X()] | registers[parameter.Y()];
                if (registers[parameter.N_Z()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[XORI] = new DefaultInstruction() {
            //XORI RX			XOR immediate value with register X. Result is stored in register X. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] ^= parameter.HHLL();
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[XOR_RY] = new DefaultInstruction() {
            //XOR RX, RY		XOR value of register Y with value of register X. Result is stored in register X. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.X()] ^= registers[parameter.Y()];
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[XOR_RZ] = new DefaultInstruction() {
            //XOR RX, RY, RZ		XOR value of register Y with value of register X. Result is stored in register Z. Affects zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                registers[parameter.N_Z()] = registers[parameter.X()] ^ registers[parameter.Y()];
                if (registers[parameter.N_Z()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
            }
        };
        instructions[MULI] = new DefaultInstruction() {
            //MULI RX			Multiply immediate value with register X. Result is stored in register X. Affects carry flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] * parameter.HHLL();
                registers[parameter.X()] = result & BOUND;
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }

            }
        };
        instructions[MUL_RY] = new DefaultInstruction() {
            //MUL RX, RY		Multiply value of register Y with value of register X. Result is stored in register X. Affects carry flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] * registers[parameter.Y()];
                registers[parameter.X()] = result & BOUND;
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[MUL_RZ] = new DefaultInstruction() {
            //MUL RX, RY, RZ		Multiply value of register Y with value of register X. Result is stored in register Z. Affects carry flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] * registers[parameter.Y()];
                registers[parameter.N_Z()] = result & BOUND;
                if (registers[parameter.N_Z()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[DIVI] = new DefaultInstruction() {
            //DIVI RX			Divide immediate value with register X. Result is stored in register X. Affects borrow flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final boolean thereIsMod = registers[parameter.X()] % parameter.HHLL() > 1;
                registers[parameter.X()] /= parameter.HHLL();
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (thereIsMod) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }

            }
        };
        instructions[DIV_RY] = new DefaultInstruction() {
            //DIV RX, RY		Divide value of register Y with value of register X. Result is stored in register X. Affects borrow flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final boolean thereIsMod = registers[parameter.X()] % registers[parameter.Y()] > 1;
                registers[parameter.X()] /= registers[parameter.Y()];
                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (thereIsMod) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[DIV_RZ] = new DefaultInstruction() {
            //DIV RX, RY, RZ		Divide value of register Y with value of register X. Result is stored in register Z. Affects borrow flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final boolean thereIsMod = registers[parameter.X()] % registers[parameter.Y()] > 1;
                registers[parameter.N_Z()] = registers[parameter.X()] / registers[parameter.Y()];
                if (registers[parameter.N_Z()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (thereIsMod) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[SHL] = new DefaultInstruction() {
            //SHL RX, N		Shift value in register X left N times. Affects borrow flag and zero flag.

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] << parameter.N_Z();
                registers[parameter.X()] = result & BOUND;

                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result > BOUND) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
        instructions[SHR] = new DefaultInstruction() {
            //SHR RX, N		Shift value in register X right N times. Affects carry flag and zero flag.*/

            @Override
            public void execute(OpCodeParameter parameter) {
                final int result = registers[parameter.X()] >> parameter.N_Z();
                registers[parameter.X()] = result & BOUND;

                if (registers[parameter.X()] == 0) {
                    flags[FLAG.ZERO.ordinal()] = true;
                } else {
                    flags[FLAG.ZERO.ordinal()] = false;
                }
                if (result < 0) {
                    flags[FLAG.CARRY_BORROW.ordinal()] = true;
                } else {
                    flags[FLAG.CARRY_BORROW.ordinal()] = false;
                }
            }
        };
    }
}