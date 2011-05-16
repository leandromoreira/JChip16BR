package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public class OpCodeParameter {
    private final short firstByte,secondByte,thirdByte;
    private final short firstByte0;
    private final short firstByte1;
    private final short secondByte0;
    private final short secondByte1;
    private final short thirdByte0;
    private final short thirdByte1;

    public OpCodeParameter(short firstByte, short secondByte, short thirdByte) {
        this.firstByte = firstByte;
        this.secondByte = secondByte;
        this.thirdByte = thirdByte;
        firstByte0 = (short) (firstByte >> 4);
        firstByte1 = (short) (firstByte & 0xF);
        secondByte0 = (short) (secondByte >> 4);
        secondByte1 = (short) (secondByte & 0xF);
        thirdByte0 = (short) (thirdByte >> 4);
        thirdByte1 = (short) (thirdByte & 0xF);
    }

    public short getFirstByte() {
        return firstByte;
    }

    public short getFirstByte0() {
        return firstByte0;
    }

    public short getFirstByte1() {
        return firstByte1;
    }

    public short getSecondByte() {
        return secondByte;
    }

    public short getSecondByte0() {
        return secondByte0;
    }

    public short getSecondByte1() {
        return secondByte1;
    }

    public short getThirdByte() {
        return thirdByte;
    }

    public short getThirdByte0() {
        return thirdByte0;
    }

    public short getThirdByte1() {
        return thirdByte1;
    }
}
