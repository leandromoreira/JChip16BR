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
    
    public static void main(String[] args) {
        int numr = 0x11;
        int numr1 = 0x13;
        int numr2 = 0x22;
        System.out.println(Integer.toHexString(numr >> 4));        
        System.out.println(Integer.toHexString(numr1 >> 4));
        System.out.println(Integer.toHexString(numr2 >> 4));
        System.out.println("=============");
        System.out.println(Integer.toHexString(numr & 0xF));
        System.out.println(Integer.toHexString(numr1 & 0xF));
        System.out.println(Integer.toHexString(numr2 & 0xF));
    }
}
