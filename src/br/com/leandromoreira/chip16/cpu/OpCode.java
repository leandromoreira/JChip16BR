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
}