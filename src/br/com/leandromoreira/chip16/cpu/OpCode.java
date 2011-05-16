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
            default:
                return "ILLEGAL";
        }
    }
/*
06 YX 0Z 00	DRW RX, RY, RZ		Draw sprite from address pointed by register Z at coordinates stored in register X and Y.  Affects carry flag.
07 0X LL HH	RND RX, HHLL		Generate a random number and store it in register X. Maximum value is HHLL.
08 00 00 00	NOP			Reserved for future updates.
09 00 00 00	SND0			Stop playing sounds.
0A 00 LL HH	SND1 HHLL		Play 500Hz tone for HHLL miliseconds.
0B 00 LL HH	SND2 HHLL		Play 1000Hz tone for HHLL miliseconds.
0C 00 LL HH	SND3 HHLL		Play 1500Hz tone for HHLL miliseconds.

10 00 LL HH	JMP HHLL		Jump to the specified address.
11 00 LL HH	JMC HHLL		Jump to the specified address if carry flag is raised.
12 00 LL HH	JMZ HHLL		Jump to the specified address if zero flag is raised.
13 YX LL HH	JME RX, RY, HHLL	Jump to the specified address if value in register X is equal to value in register Y.
14 00 LL HH	CALL HHLL		Call subroutine at the specified address. Store PC to stack beforehand. Increase SP by 2.
15 00 00 00	RET			Return from a subroutine. Get PC from stack. Decrease SP by 2.

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
B1 0X 0N 00	SHR RX, N		Shift value in register X right N times. Affects carry flag and zero flag.*/

    private static String hexa(short value) {
        return Integer.toHexString(value).toUpperCase();
    }
}