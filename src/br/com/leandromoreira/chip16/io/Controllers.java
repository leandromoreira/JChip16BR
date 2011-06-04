package br.com.leandromoreira.chip16.io;

import br.com.leandromoreira.chip16.cpu.Memory;
import br.com.leandromoreira.chip16.cpu.MemoryMap;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

/**
 * @author leandro-rm
 */
public class Controllers {
    private final int MASK = 0x00FF;
    private final static int BUTTON_UP = 0;
    private final static int BUTTON_DOWN = 1;
    private final static int BUTTON_LEFT = 2;
    private final static int BUTTON_RIGHT = 3;
    private final static int BUTTON_SELECT = 4;
    private final static int BUTTON_START = 5;
    private final static int BUTTON_A = 6;
    private final static int BUTTON_B = 7;
    private final static int[] B =  new int[]{0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80};
    private final Map<Integer, Integer> mapping1 = new HashMap<Integer, Integer>();
    private final Map<Integer, Integer> mapping2 = new HashMap<Integer, Integer>();
    private final List<Integer> iobuffer = Collections.synchronizedList(new ArrayList<Integer>());
    private final Memory memory;

    public Controllers(final Memory memory) {
        mapping1.put(BUTTON_UP, 38);
        mapping1.put(BUTTON_DOWN, 40);
        mapping1.put(BUTTON_LEFT, 37);
        mapping1.put(BUTTON_RIGHT, 39);
        mapping1.put(BUTTON_START, 10);
        mapping1.put(BUTTON_SELECT, 32);
        mapping1.put(BUTTON_A, 90);
        mapping1.put(BUTTON_B, 88);

        mapping2.put(BUTTON_UP, 380);
        mapping2.put(BUTTON_DOWN, 400);
        mapping2.put(BUTTON_LEFT, 370);
        mapping2.put(BUTTON_RIGHT, 390);
        mapping2.put(BUTTON_START, 100);
        mapping2.put(BUTTON_SELECT, 320);
        mapping2.put(BUTTON_A, 900);
        mapping2.put(BUTTON_B, 880);
        this.memory = memory;
    }

    public boolean isAValidButtonForMachine(final Integer keyCode) {
        return mapping1.containsValue(keyCode) | mapping2.containsValue(keyCode);
    }

    public void addCommand(final Integer keyCode) {
        if (isAValidButtonForMachine(keyCode)) {
            iobuffer.add(keyCode);
        }
    }

    public void removeCommand(final Integer keyCode) {
        if (isAValidButtonForMachine(keyCode)) {
            iobuffer.remove(keyCode);
        }
    }

    public void update() {
        /*final short value1 = composeValue(mapping1);
        final short value2 = composeValue(mapping2);
        System.out.println(value1);
        System.out.println(value2);*/
        memory.writeAt(MemoryMap.CONTROLLER_ONE,(short)B[new Random().nextInt(8)]);        
        System.out.println(iobuffer);
        //memory.writeAt(MemoryMap.CONTROLLER_TWO,value2);        
    }

    private short composeValue(Map<Integer, Integer> mapping) {
        short value = 0;        
        for (final Integer pressedKey : iobuffer) {
            switch(pressedKey){
                case BUTTON_UP:
                    value =  0x1 & MASK;
                    break;
                case BUTTON_DOWN:
                    break;
                case BUTTON_LEFT:
                    break;
                case BUTTON_RIGHT:
                    break;
                case BUTTON_SELECT:
                    break;
                case BUTTON_START:
                    break;
                case BUTTON_A:
                    break;
                case BUTTON_B:    
                    break;
            }
        }
        return value;
    }
}