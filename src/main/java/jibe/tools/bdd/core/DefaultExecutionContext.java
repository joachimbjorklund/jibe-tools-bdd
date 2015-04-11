package jibe.tools.bdd.core;

import com.google.common.base.Optional;
import jibe.tools.bdd.api.ExecutionContext;
import jibe.tools.bdd.api.ExecutionContextKeyValue;
import org.apache.commons.lang.builder.ToStringBuilder;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Objects;
import java.util.Set;

/**
 *
 */
public class DefaultExecutionContext implements ExecutionContext {
    private final HashMap<String, Object> ctx = new HashMap<>();

    private DefaultExecutionContext() {
    }

    public static Builder builder() {
        return new Builder();
    }

    @Override
    public Object get(String key) {
        return ctx.get(key);
    }

    @Override
    public Object getOr(String key, Object defaultValue) {
        if (find(key).isPresent()) {
            return find(key).get();
        }
        return defaultValue;
    }

    @Override
    public Object put(String key, Object value) {
        return ctx.put(key, value);
    }

    @Override
    public Object put(Object value) {
        if (value == null) {
            return null;
        }
        return put(value.getClass().getName(), value);
    }

    @Override
    public Object remove(String key) {
        return ctx.remove(key);
    }

    @Override
    public <T> Set<ExecutionContextKeyValue<T>> findByType(Class<T> type) {
        Set<ExecutionContextKeyValue<T>> answer = new HashSet<>();
        for (Map.Entry<String, Object> e : ctx.entrySet()) {
            Object value = e.getValue();
            if (type.isAssignableFrom(value.getClass())) {
                answer.add(new DefaultExecutionContextKeyValue(e.getKey(), value));
            }
        }
        return answer;
    }

    @Override
    public <T> T requiresOneOfType(Class<T> type) {
        Iterator<ExecutionContextKeyValue<T>> iterator = findByType(type).iterator();
        if (!iterator.hasNext()) {
            throw new NoSuchElementException("of type: " + type);
        }
        ExecutionContextKeyValue<T> answer = iterator.next();
        if (iterator.hasNext()) {
            throw new RuntimeException("requiresOneOfType: found more than one of type: " + type);
        }
        return answer.getValue();
    }

    @Override
    public <T> ExecutionContextKeyValue<T> findByKey(String key, Class<T> type) {
        Set<ExecutionContextKeyValue<T>> byType = findByType(type);
        Iterator<ExecutionContextKeyValue<T>> iterator = byType.iterator();
        while (iterator.hasNext()) {
            ExecutionContextKeyValue<T> next = iterator.next();
            if (next.getKey().equals(key)) {
                return new DefaultExecutionContextKeyValue<>(key, next.getValue());
            }
        }
        return null;
    }

    @Override
    public Optional<Object> find(String key) {
        return Optional.fromNullable(get(key));
    }

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }

    public static class Builder {
        private DefaultExecutionContext ctx = new DefaultExecutionContext();

        public Builder put(String key, Object value) {
            ctx.put(key, value);
            return this;
        }

        public Builder put(Object value) {
            ctx.put(value);
            return this;
        }

        public ExecutionContext build() {
            return ctx;
        }
    }

    private class DefaultExecutionContextKeyValue<T> implements ExecutionContextKeyValue {
        private final String key;
        private final T value;

        public DefaultExecutionContextKeyValue(String key, T value) {
            this.key = key;
            this.value = value;
        }

        public String getKey() {
            return key;
        }

        public T getValue() {
            return value;
        }

        @Override
        public boolean equals(Object o) {
            return (this == o) || ((o instanceof DefaultExecutionContextKeyValue)
                    && Objects.equals(((DefaultExecutionContextKeyValue) o).key, this.key));
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(key);
        }
    }
}
