package jibe.tools.bdd.api;

/**
 *
 */
public interface ExecutionContext {
    Object get(String key);

    void put(String key, Object value);
}
