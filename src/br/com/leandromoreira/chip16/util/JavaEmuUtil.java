package br.com.leandromoreira.chip16.util;

import java.nio.ByteBuffer;

/**
 * steal from JChip8Br & JNesBR
 * @author leandro-rm
 */
public class JavaEmuUtil {

    public static short readUnsignedByte(ByteBuffer readbuffer) {
        return (short) (readbuffer.get() & 0xff);
    }
}
