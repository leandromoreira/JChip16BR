package br.com.leandromoreira.chip16.rom;

import java.nio.ByteBuffer;

/**
 * @author leandro-rm
 */
public interface ROM {
    long getLength();
    ByteBuffer getRom();
    String getTitleName();
}
