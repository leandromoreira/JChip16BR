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
    private Memory memory;
    private int controllerAddress;

    public Controller(Memory memory, int controllerAddress) {
        this.memory = memory;
        this.controllerAddress = controllerAddress;
    }
    
    public void press(int keyCode){
        int key = mapping.get(keyCode);
        switch(key){
            case BUTTON_UP:
        }
        memory.writeAt(this.controllerAddress,(short)keyCode);
    }
    
}
