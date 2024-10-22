package at.kaindorf.io;

import lombok.NoArgsConstructor;

@NoArgsConstructor
public class JsonNull extends JsonValue {
    @Override
    public String toJson() {
        return "null";
    }

    @Override
    public <T> T toObject(Class<T> clazz, Class<?> parent) {
        return null;
    }
}
