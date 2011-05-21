package br.com.leandromoreira.chip16.debug;

import java.util.ArrayList;
import java.util.List;

/**
 * @author leandro-rm
 */
public class Debugger {
    public static final String DEBUGGER_CHR = "♦";
    private final List<Breakpoint> breakpoints = new ArrayList<Breakpoint>();
    public void add(final Breakpoint breakpoint){
        breakpoints.add(breakpoint);
    }
    public void remove(final Breakpoint breakpoint){
        breakpoints.remove(breakpoint);
    }
}
