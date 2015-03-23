package jibe.tools.bdd.api;

/**
 *
 */
abstract public class AbstractExecution<T> implements Execution {

    @Override
    public void preExecute(ExecutionContext ctx) {
    }

    @Override
    abstract public T execute(ExecutionContext ctx);

    @Override
    public void postExecute(ExecutionContext ctx) {
    }

}
