package br.com.leandromoreira.chip16.rom;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;

/**
 * @author leandro-rm
 */
public class Loader {

    public static ByteBuffer load(File file) {
        FileChannel fileChannel;
        try {
            fileChannel = new RandomAccessFile(file, "r").getChannel();
            try {
                return fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, (int) fileChannel.size());
            } catch (IOException ex) {
                throw new IllegalArgumentException("There was an io error while reading "+file,ex);
            }
        } catch (FileNotFoundException ex) {
            throw new IllegalArgumentException("File ("+file+") not found!",ex);
        }
    }
}
