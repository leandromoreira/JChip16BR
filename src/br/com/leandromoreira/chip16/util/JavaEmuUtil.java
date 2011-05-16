package br.com.leandromoreira.chip16.util;

import java.nio.ByteBuffer;

/**
 * steal from JChip8Br & JNesBR
 * @author leandro-rm
 */
public class JavaEmuUtil {

    public static short readUnsignedByte(final ByteBuffer readbuffer) {
        return (short) (readbuffer.get() & 0xff);
    }
    public static String getHexadecimal2Formatted(short value){
        return formatHexadecimalWith(2,value);
    }
    public static String getHexadecimal4Formatted(int value){
        return formatHexadecimalWith(4,value);
    }

    private static String formatHexadecimalWith(int size,int value) {
        int hexaSize = Integer.toHexString(value).toUpperCase().length();
        String address = "0x";
        for (int i = 0; i < (size-hexaSize) ; i++){
            address += "0";
        }
        return address + Integer.toHexString(value).toUpperCase();
    }
}