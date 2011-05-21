package br.com.leandromoreira.chip16.cpu;

import br.com.leandromoreira.chip16.util.JavaEmuUtil;

/**
 * @author leandro-rm
 */
public class OpCodeParameter {
    private final short ll,hh;
    private final short y;
    private final short x;
    private final short nOrZ;

    public OpCodeParameter(final short firstByte,final short secondByte,final short thirdByte) {
        this.ll = secondByte;
        this.hh = thirdByte;
        y = (short) (firstByte >> 4);
        x = (short) (firstByte & 0xF);
        nOrZ = (short) (secondByte & 0xF);
    }

    public short Y() {
        return y;
    }

    public short X() {
        return x;
    }

    public short LL() {
        return ll;
    }

    public short N_Z() {
        return nOrZ;
    }

    public short HH() {
        return hh;
    }
    
    public int HHLL(){
        return JavaEmuUtil.getLittleEndian(LL(), HH());
    }
}