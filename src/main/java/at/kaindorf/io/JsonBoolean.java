package at.kaindorf.io;

import lombok.AllArgsConstructor;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class JsonBoolean extends JsonValue {
    private final boolean value;

    @Override
    public String toJson() {
        return Boolean.toString(value);
    }

    @Override
    public <T> T toObject(Class<T> clazz, Class<?> parent) {
        if (!clazz.equals(Boolean.class) && !clazz.equals(boolean.class)) {
            throw new RuntimeException("Cannot convert JsonBoolean to '" + clazz.getName() + "'");
        }

        return clazz.cast(value);
    }
}
