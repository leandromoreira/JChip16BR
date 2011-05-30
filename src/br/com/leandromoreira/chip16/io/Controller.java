package br.com.leandromoreira.chip16.io;

import br.com.leandromoreira.chip16.cpu.Memory;
import br.com.leandromoreira.chip16.cpu.MemoryMap;
import java.util.HashMap;
import java.util.Map;

/**
 * @author leandro-rm
 */
public class Controller {
    private final static int BUTTON_UP = 0;
    private final static int BUTTON_DOWN = 1;
    private final static int BUTTON_LEFT = 2;
    private final static int BUTTON_RIGHT = 3;
    private final static int BUTTON_SELECT = 4;
    private final static int BUTTON_START = 5;
    private final static int BUTTON_A = 6;
    private final static int BUTTON_B = 7;
    private Map<Integer,Integer> mapping = new HashMap<Integer, Integer>();
    
    public Controller(){
        mapping.put(BUTTON_UP, 38);
        mapping.put(BUTTON_DOWN, 40);
        mapping.put(BUTTON_LEFT, 37);
        mapping.put(BUTTON_RIGHT, 39);
        mapping.put(BUTTON_START, 10);
        mapping.put(BUTTON_SELECT, 32);
        mapping.put(BUTTON_A, 90);
        mapping.put(BUTTON_B, 88);
    }
}