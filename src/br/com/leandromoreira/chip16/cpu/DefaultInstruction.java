package br.com.leandromoreira.chip16.cpu;

/**
 * @author leandro-rm
 */
public class DefaultInstruction implements Instruction{
    private final OpCode opCode;
    private final Executor executor; 
    public DefaultInstruction(final OpCode opCode,final Executor executor){
        this.opCode = opCode;
        this.executor = executor;
    }
    
    @Override
    public void execute(OpCodeParameter parameter) {
        executor.execute(parameter);
    }

    @Override
    public String getAssembler() {
        return opCode.name();
    }

    @Override
    public int getCycle() {
        return 1;
    }
    
}
