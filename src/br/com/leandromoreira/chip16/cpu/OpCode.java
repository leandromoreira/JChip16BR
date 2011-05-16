package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public enum OpCode {
    NOP,CLS,VBLNK,BGC,SPR,DRW_HHL,DRW_RZ,RND,NOP_FUTURE,SND0,SND1,SND2,SND3,
    JMP,JMC,JMZ,JME,CALL,RET,
    LDI_RX,LDI_SP,LDM_HHLL,LDM_RY,MOV,
    STM_HHLL,STM_RY,
    ADDI,ADD_RY,ADD_RZ,
    SUBI,SUB_RY,SUB_RZ,
    ANDI,AND_RY,AND_RZ, 
    ORI,OR_RY,OR_RZ,
    XORI,XOR_RY,XOR_RZ,
    MULI,MUL_RY,MUL_RZ,
    DIVI,DIV_RY,DIV_RZ,
    SHL,SHR;

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
                return "SPR "+hexa(thirdByte)+hexa(secondByte);
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
                return "JMP "+hexa(thirdByte)+hexa(secondByte);                
            case 0X11:
                return "JMC "+hexa(thirdByte)+hexa(secondByte);                                
            case 0X12:
                return "JMZ "+hexa(thirdByte)+hexa(secondByte);                                
            case 0X13:
                return "JME R"+hexa(firstByte1)+", R"+hexa(firstByte0) +" , "+hexa(thirdByte)+hexa(secondByte);                                
            case 0X14:
                return "CALL "+hexa(thirdByte)+hexa(secondByte);                                
            case 0X15:
                return "RET"; 
            case 0X20:
                return "LDI R"+hexa(firstByte1)+", "+hexa(thirdByte)+hexa(secondByte);
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
                return "ADDI R"+hexa(firstByte1);                
            case 0X41:
                return "ADD R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X42:
                return "ADD R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                                
            case 0X50:
                return "SUBI R"+hexa(firstByte1);                
            case 0X51:
                return "SUB R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X52:
                return "SUB R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0X60:
                return "ANDI R"+hexa(firstByte1);                
            case 0X61:
                return "AND R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X62:
                return "AND R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0X70:
                return "ORI R"+hexa(firstByte1);                
            case 0X71:
                return "OR R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X72:
                return "OR R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0X80:
                return "XORI R"+hexa(firstByte1);                
            case 0X81:
                return "XOR R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X82:
                return "XOR R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0X90:
                return "MULI R"+hexa(firstByte1);                
            case 0X91:
                return "MUL R"+hexa(firstByte1)+", R"+hexa(firstByte0);                
            case 0X92:
                return "MUL R"+hexa(firstByte1)+", R"+hexa(firstByte0) + ", R"+hexa(secondByte1);                
            case 0XA0:
                return "DIVI R"+hexa(firstByte1);                
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
}