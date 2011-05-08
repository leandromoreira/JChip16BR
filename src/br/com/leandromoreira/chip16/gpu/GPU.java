package br.com.leandromoreira.chip16.gpu;

import java.awt.Color;
import java.util.Map;

/**
 * @author leandro-rm
 */
public class GPU {
    private final static int NUMBER_OF_COLORS = 16;
    private final static int WIDTH = 320;
    private final static int HEIGHT = 240;
    private Map<Integer,Color> colors;
    
    public void init(){
        colors.put(0x0, Color.BLACK);
/*
0x1 - 0x000000 (Black)
0x2 - 0x888888 (Gray)
0x3 - 0xBF3932 (Red)
0x4 - 0xDE7AAE (Pink)
0x5 - 0x4C3D21 (Dark brown)
0x6 - 0x905F25 (Brown)
0x7 - 0xE49452 (Orange)
0x8 - 0xEAD979 (Yellow)
0x9 - 0x537A3B (Green)
0xA - 0xABD54A (Light green)
0xB - 0x252E38 (Dark blue)
0xC - 0x00467F (Blue)
0xD - 0x68ABCC (Light blue)
0xE - 0xBCDEE4 (Sky blue)
0xF - 0xFFFFFF (White)*/
    }
    
    public void clear(){
    }
}
