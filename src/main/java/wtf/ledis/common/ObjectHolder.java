package wtf.ledis.common;

import wtf.ledis.io.Reader;
import wtf.ledis.io.Writer;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class ObjectHolder {
    private static volatile ObjectHolder instance;

    private final Map<Class<?>, Object> instances;

    private ObjectHolder() {
        var writer = new Writer();
        this.instances = new HashMap<>();
        instances.put(Writer.class, writer);
        instances.put(Reader.class, new Reader());
    }

    public static ObjectHolder getInstance() {
        if (instance == null) {
            synchronized (ObjectHolder.class) {
                if (instance == null) {
                    instance = new ObjectHolder();
                }
            }
        }

        return instance;
    }

    @SuppressWarnings("unchecked")
    public <T> T get(Class<T> type) {
        var obj = instances.get(Objects.requireNonNull(type));
        if (obj == null) {
            throw new IllegalArgumentException("No object found for type " + type.getSimpleName());
        }

        return (T) obj;
    }
}
