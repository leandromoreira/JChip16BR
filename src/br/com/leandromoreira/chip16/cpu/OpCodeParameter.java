package br.com.leandromoreira.chip16.cpu;

import br.com.leandromoreira.chip16.util.JavaEmuUtil;

/**
 * @author leandro-rm
 */
public class OpCodeParameter {
    private final short secondByte,thirdByte;
    private final short firstByte0;
    private final short firstByte1;
    private final short secondByte1;

    public OpCodeParameter(short firstByte, short secondByte, short thirdByte) {
        this.secondByte = secondByte;
        this.thirdByte = thirdByte;
        firstByte0 = (short) (firstByte >> 4);
        firstByte1 = (short) (firstByte & 0xF);
        secondByte1 = (short) (secondByte & 0xF);
    }

    public short Y() {
        return firstByte0;
    }

    public short X() {
        return firstByte1;
    }

    public short LL() {
        return secondByte;
    }

    public short N_Z() {
        return secondByte1;
    }

    public short HH() {
        return thirdByte;
    }
    
    public int HHLL(){
        return JavaEmuUtil.getLittleEndian(LL(), HH());
    }
}