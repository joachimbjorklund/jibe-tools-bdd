package jibe.tools.bdd.api;

import com.google.common.base.Optional;

import java.util.Set;

/**
 *
 */
public interface ExecutionContext {
    Object get(String key);

    Object getOr(String key, Object defaultValue);

    Optional<Object> find(String key);

    Object put(String key, Object value);

    Object remove(String key);

    <T> Set<ExecutionContextKeyValue<T>> findByType(Class<T> type);

    <T> T requiresOneByType(Class<T> type);

    <T> ExecutionContextKeyValue<T> findByKey(String key, Class<T> type);
}
