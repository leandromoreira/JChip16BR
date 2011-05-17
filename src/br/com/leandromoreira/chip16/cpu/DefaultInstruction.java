package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public class DefaultInstruction implements Instruction{
    private final Executor executor; 
    public DefaultInstruction(final Executor executor){
        this.executor = executor;
    }
    
    @Override
    public void execute(OpCodeParameter parameter) {
        executor.execute(parameter);
    }

    @Override
    public int addToPC() {
        return 4;
    }
}
