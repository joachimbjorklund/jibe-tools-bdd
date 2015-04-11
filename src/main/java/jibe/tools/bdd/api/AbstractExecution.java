package jibe.tools.bdd.api;

import com.google.common.base.Optional;

/**
 *
 */
abstract public class AbstractExecution<T> implements Execution {

    private Throwable exception;

    @Override
    public void preExecute(ExecutionContext ctx) {
    }

    @Override
    abstract public T execute(ExecutionContext ctx);

    @Override
    public void postExecute(ExecutionContext ctx) {
    }

    @Override
    public void exception(Throwable e) {
        exception = e;
    }

    @Override
    public Optional<Throwable> exception() {
        return Optional.fromNullable(exception);
    }

    @Override
    public String describe() {
        return this.getClass().getName();
    }

    @Override
    public DescriptiveType descriptiveType() {
        return DescriptiveType.Execution;
    }
}
