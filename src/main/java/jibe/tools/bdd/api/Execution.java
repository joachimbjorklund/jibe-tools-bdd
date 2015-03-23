package jibe.tools.bdd.api;

/**
 *
 */
public interface Execution<T> {
    void preExecute(ExecutionContext ctx);

    T execute(ExecutionContext ctx);

    void postExecute(ExecutionContext ctx);
}
