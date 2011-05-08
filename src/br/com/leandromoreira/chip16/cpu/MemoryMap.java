package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public class MemoryMap {
    public final static int ROM_START = 0x0000;
    public final static int ROM_END = 0xFDF0 - 1;
        /*0x0000 - Start of ROM.
0xFDF0 - Start of stack (32 levels).
0xFFF0 - IO ports.
Controller 1: FFF0.
Controller 2: FFF2.*/
    public final static int CONTROLLER_ONE = 0xFFF0;
    public final static int CONTROLLER_TWO = 0xFFF2;
}
