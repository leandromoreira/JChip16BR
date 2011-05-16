package br.com.leandromoreira.chip16.cpu;

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

    public static String assembler(int opCode,short firstByte, short secondByte, short thirdByte) {
        final short firstByte0 = (short) (firstByte >> 4);
        final short firstByte1 = (short) (firstByte & 0xF);
        final short secondByte0 = (short) (secondByte >> 4);
        final short secondByte1 = (short) (secondByte & 0xF);
        final short thirdByte0 = (short) (thirdByte >> 4);
        final short thirdByte1 = (short) (thirdByte & 0xF);
        switch(opCode){
            case 0:
                return "NOP";
            case 1:
                return "CLS";
            case 2:
                return "VBLNK";
            case 3:
                return "BGC "+hexa(secondByte1); 
            case 4:
                return "SPR #"+hexa2(thirdByte)+hexa2(secondByte);
            case 5:
                return "DRW R"+hexa(firstByte1)+", R"+hexa(firstByte0)+", "+hexa(thirdByte)+hexa(secondByte);
            case 6:
                return "DRW R"+hexa(firstByte1)+", R"+hexa(firstByte0)+", R"+hexa(secondByte1);
            case 7:
                return "RND R"+hexa(firstByte1)+", "+hexa(thirdByte)+hexa(secondByte);
            case 8:
                return "NOP";
            case 9:
                return "SND0";              
            case 0XA:
                return "SND1 "+hexa(thirdByte)+hexa(secondByte);                
            case 0XB:
                return "SND2 "+hexa(thirdByte)+hexa(secondByte);                
            case 0XC:
                return "SND3 "+hexa(thirdByte)+hexa(secondByte);
            case 0X10:
                return "JMP #"+hexa(thirdByte)+hexa(secondByte);                
            case 0X11:
                return "JMC #"+hexa(thirdByte)+hexa(secondByte);                                
            case 0X12:
                return "JMZ #"+hexa(thirdByte)+hexa(secondByte);                                
            case 0X13:
                return "JME R"+hexa(firstByte1)+", R"+hexa(firstByte0) +" , #"+hexa(thirdByte)+hexa(secondByte);                                
            case 0X14:
                return "CALL #"+hexa(thirdByte)+hexa(secondByte);                                
            case 0X15:
                return "RET"; 
            case 0X20:
                return "LDI R"+hexa(firstByte1)+", #"+ (hexa(thirdByte)+hexa(secondByte));
            case 0X21:
                return "LDI SP, "+hexa(thirdByte)+hexa(secondByte);                
            case 0X22:
                return "LDM R"+hexa(firstByte1)+", "+hexa(thirdByte)+hexa(secondByte);                
            case 0X23:
                return "LDM R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X24:
                return "MOV R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X30:
                return "STM R"+hexa(firstByte1)+", "+hexa(thirdByte)+hexa(secondByte);                
            case 0X31:
                return "STM R"+hexa(firstByte1)+", R"+hexa(firstByte0);                                
            case 0X40:
                return "ADDI R"+hexa(firstByte1)+", #"+(hexa(thirdByte)+hexa(secondByte));                
            case 0X41:
                return "ADD R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X42:
                return "ADD R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                                
            case 0X50:
                return "SUBI R"+hexa(firstByte1)+", #"+(hexa(thirdByte)+hexa(secondByte));                
            case 0X51:
                return "SUB R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X52:
                return "SUB R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0X60:
                return "ANDI R"+hexa(firstByte1)+", #"+(hexa(thirdByte)+hexa(secondByte));                
            case 0X61:
                return "AND R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X62:
                return "AND R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0X70:
                return "ORI R"+hexa(firstByte1)+", #"+(hexa(thirdByte)+hexa(secondByte));                
            case 0X71:
                return "OR R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X72:
                return "OR R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0X80:
                return "XORI R"+hexa(firstByte1)+", #"+(hexa(thirdByte)+hexa(secondByte));                
            case 0X81:
                return "XOR R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X82:
                return "XOR R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0X90:
                return "MULI R"+hexa(firstByte1)+", #"+(hexa(thirdByte)+hexa(secondByte));                
            case 0X91:
                return "MUL R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X92:
                return "MUL R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0XA0:
                return "DIVI R"+hexa(firstByte1)+", #"+(hexa(thirdByte)+hexa(secondByte));                
            case 0XA1:
                return "DIV R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0XA2:
                return "DIV R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0XB0:
                return "SHL R"+hexa(firstByte1)+", "+hexa(secondByte1);                
            case 0XB1:
                return "SHR R"+hexa(firstByte1)+", "+hexa(secondByte1);                                
            default:
                return "ILLEGAL";
        }
    }
    private static String hexa(short value) {
        return Integer.toHexString(value).toUpperCase();
    }

    private static String hexa2(short value) {
        return hexa(value).length()==1?"0"+hexa(value):hexa(value);
    }
}