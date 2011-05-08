package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public class MemoryMap {
    public final static int ROM_START = 0x0000;
    public final static int ROM_END = 0xFDF0 - 1;
    public final static int STACK_START = 0xFDF0;
    public final static int STACK_END = 0xFDF0 + 32;
    public final static int CONTROLLER_ONE = 0xFFF0;
    public final static int CONTROLLER_TWO = 0xFFF2;
}
