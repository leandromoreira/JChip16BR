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
    private final static int[] BUTTON_MASK =  new int[]{0x01,0x02,0x04,0x08,0x10,0x20,0x40,0x80};
    private final Map<Integer, Integer> mapping1 = new HashMap<Integer, Integer>();
    private final Map<Integer, Integer> mapping2 = new HashMap<Integer, Integer>();
    private final List<Integer> iobuffer = new ArrayList<Integer>();
    private final Memory memory;

    public Controllers(final Memory memory) {
        mapping1.put(38, BUTTON_UP);
        mapping1.put(40, BUTTON_DOWN);
        mapping1.put(37, BUTTON_LEFT);
        mapping1.put(39, BUTTON_RIGHT);
        mapping1.put(10, BUTTON_START);
        mapping1.put(32, BUTTON_SELECT);
        mapping1.put(90, BUTTON_A);
        mapping1.put(88, BUTTON_B);

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
        return mapping1.containsKey(keyCode) | mapping2.containsKey(keyCode);
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
       memory.writeAt(MemoryMap.CONTROLLER_ONE,composeValue(mapping1, new ArrayList<Integer>(iobuffer)));        
    }

    private short composeValue(Map<Integer, Integer> mapping,final List<Integer> buffer) {
        short value = 0;        
        for (final Integer pressedKey : iobuffer) {
            System.out.println(pressedKey);
            System.out.println(mapping.get(pressedKey));
            value = (short) BUTTON_MASK[mapping.get(pressedKey)];
        }
        return value;
    }
}