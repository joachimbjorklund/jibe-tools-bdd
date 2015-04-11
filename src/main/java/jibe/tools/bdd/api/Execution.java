package jibe.tools.bdd.api;

import com.google.common.base.Optional;

/**
 *
 */
public interface Execution<T> extends Descriptive {
    void preExecute(ExecutionContext ctx);

    T execute(ExecutionContext ctx);

    void postExecute(ExecutionContext ctx);

    void exception(Throwable e);

    Optional<Throwable> exception();
}
