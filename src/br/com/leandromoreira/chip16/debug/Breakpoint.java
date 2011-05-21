package br.com.leandromoreira.chip16.debug;

/**
 * @author leandro-rm
 */
public class Breakpoint {
    private final int programCounter;

    public Breakpoint(final int programCounter) {
        this.programCounter = programCounter;
    }
    public int getProgramCounter() {
        return programCounter;
    }

    @Override
    public boolean equals(final Object obj) {
        if (obj == null) {
            return false;
        }
        if (getClass() != obj.getClass()) {
            return false;
        }
        final Breakpoint other = (Breakpoint) obj;
        if (this.programCounter != other.programCounter) {
            return false;
        }
        return true;
    }

    @Override
    public int hashCode() {
        int hash = 3;
        hash = 71 * hash + this.programCounter;
        return hash;
    }

    @Override
    public String toString() {
        return "Breakpoint{" + "programCounter=" + programCounter + '}';
    }
    
}