package br.com.leandromoreira.chip16.cpu;

import br.com.leandromoreira.chip16.util.JavaEmuUtil;

/**
 * @author leandro-rm
 */
public class OpCode {
    public static final int NOP = 0x00;
    public static final int CLS = 0x01;
    public static final int VBLNK = 0x02;
    public static final int BGC = 0x03;
    public static final int SPR = 0x04;
    public static final int DRW_HHL = 0x05;
    public static final int DRW_RZ = 0x06;
    public static final int RND = 0x07;
    public static final int NOP_FUTURE = 0x08;
    public static final int SND0 = 0x09;
    public static final int SND1 = 0x0A;
    public static final int SND2 = 0x0B;
    public static final int SND3 = 0x0C;
    public static final int JMP = 0x10;
    public static final int JMC = 0x11;
    public static final int JMZ = 0x12;
    public static final int JME = 0x13;
    public static final int CALL = 0x14;
    public static final int RET = 0x15;
    public static final int LDI_RX = 0x20;
    public static final int LDI_SP = 0x21;
    public static final int LDM_HHLL = 0x22;
    public static final int LDM_RY = 0x23;
    public static final int MOV = 0x24;
    public static final int STM_HHLL = 0x30;
    public static final int STM_RY = 0x31;
    public static final int ADDI = 0x40;
    public static final int ADD_RY = 0x41;
    public static final int ADD_RZ = 0x42;
    public static final int SUBI = 0x50;
    public static final int SUB_RY = 0x51;
    public static final int SUB_RZ = 0x52;
    public static final int ANDI = 0x60;
    public static final int AND_RY = 0x61;
    public static final int AND_RZ = 0x62;
    public static final int ORI = 0x70;
    public static final int OR_RY = 0x71;
    public static final int OR_RZ = 0x72;
    public static final int XORI = 0x80;
    public static final int XOR_RY = 0x81;
    public static final int XOR_RZ = 0x82;
    public static final int MULI = 0x90;
    public static final int MUL_RY = 0x91;
    public static final int MUL_RZ = 0x92;
    public static final int DIVI = 0xA0;
    public static final int DIV_RY = 0xA1;
    public static final int DIV_RZ =0xA2;
    public static final int SHL = 0xB0;
    public static final int SHR = 0xB1;

    public static String assembler(final int opCode,final short firstByte, short secondByte,final short thirdByte) {
        final OpCodeParameter parameter = new OpCodeParameter(firstByte, secondByte, thirdByte);
        switch(opCode){
            case NOP:
                return "NOP";
            case CLS:
                return "CLS";
            case VBLNK:
                return "VBLNK";
            case BGC:
                return "BGC "+(parameter.N_Z()); 
            case SPR:
                return "SPR #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case DRW_HHL:
                return "DRW R"+hexa(parameter.X())+", R"+hexa(parameter.Y())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case DRW_RZ:
                return "DRW R"+hexa(parameter.X())+", R"+hexa(parameter.Y())+", R"+hexa(parameter.N_Z());
            case RND:
                return "RND R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case NOP_FUTURE:
                return "NOP";
            case SND0:
                return "SND0";              
            case SND1:
                return "SND1 "+JavaEmuUtil.getLittleEndian(secondByte, thirdByte);                
            case SND2:
                return "SND2 "+JavaEmuUtil.getLittleEndian(secondByte, thirdByte);                
            case SND3:
                return "SND3 "+JavaEmuUtil.getLittleEndian(secondByte, thirdByte);
            case JMP:
                return "JMP #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case JMC:
                return "JMC #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                                
            case JMZ:
                return "JMZ #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                                
            case JME:
                return "JME R"+hexa(parameter.X())+", R"+hexa(parameter.Y()) +" , #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                                
            case CALL:
                return "CALL #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                                
            case RET:
                return "RET"; 
            case LDI_RX:
                return "LDI R"+hexa(parameter.X())+", #"+ (hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));
            case LDI_SP:
                return "LDI SP, #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case LDM_HHLL:
                return "LDM R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case LDM_RY:
                return "LDM R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case MOV:
                return "MOV R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case STM_HHLL:
                return "STM R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case STM_RY:
                return "STM R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                                
            case ADDI:
                return "ADDI R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case ADD_RY:
                return "ADD R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case ADD_RZ:
                return "ADD R"+hexa(parameter.X())+", R"+hexa(parameter.Y()) + ", R"+hexa(parameter.N_Z());                                
            case SUBI:
                return "SUBI R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case SUB_RY:
                return "SUB R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case SUB_RZ:
                return "SUB R"+hexa(parameter.X())+", R"+hexa(parameter.Y()) + ", R"+hexa(parameter.N_Z());                
            case ANDI:
                return "ANDI R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case AND_RY:
                return "AND R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case AND_RZ:
                return "AND R"+hexa(parameter.X())+", R"+hexa(parameter.Y()) + ", R"+hexa(parameter.N_Z());                
            case ORI:
                return "ORI R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case OR_RY:
                return "OR R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case OR_RZ:
                return "OR R"+hexa(parameter.X())+", R"+hexa(parameter.Y()) + ", R"+hexa(parameter.N_Z());                
            case XORI:
                return "XORI R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case XOR_RY:
                return "XOR R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case XOR_RZ:
                return "XOR R"+hexa(parameter.X())+", R"+hexa(parameter.Y()) + ", R"+hexa(parameter.N_Z());                
            case MULI:
                return "MULI R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case MUL_RY:
                return "MUL R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case MUL_RZ:
                return "MUL R"+hexa(parameter.X())+", R"+hexa(parameter.Y()) + ", R"+hexa(parameter.N_Z());                
            case DIVI:
                return "DIVI R"+hexa(parameter.X())+", #"+(hexa(JavaEmuUtil.getLittleEndian(secondByte, thirdByte)));                
            case DIV_RY:
                return "DIV R"+hexa(parameter.X())+", R"+hexa(parameter.Y());                
            case DIV_RZ:
                return "DIV R"+hexa(parameter.X())+", R"+hexa(parameter.Y()) + ", R"+hexa(parameter.N_Z());                
            case SHL:
                return "SHL R"+hexa(parameter.X())+", "+parameter.N_Z();                
            case SHR:
                return "SHR R"+hexa(parameter.X())+", "+parameter.N_Z();                                
            default:                
                throw new IllegalArgumentException("Unkwown opcode!");
        }
    }
    private static String hexa(short value) {
        return Integer.toHexString(value).toUpperCase();
    }
    private static String hexa(int value) {
        return Integer.toHexString(value).toUpperCase();
    }    
}