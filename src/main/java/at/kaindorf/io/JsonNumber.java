package at.kaindorf.io;

import lombok.AllArgsConstructor;
import lombok.NonNull;
import lombok.ToString;

@ToString
@AllArgsConstructor
public class JsonNumber extends JsonValue {
    @NonNull
    private final Number value;

    @Override
    public String toJson() {
        if ((value instanceof Double || value instanceof Float)
                && Double.isNaN(value.doubleValue()) || Double.isInfinite(value.doubleValue())) {
            throw new RuntimeException("NaN and Infinite are invalid values");
        }

        return switch (value) {
            case Double ignored -> Double.toString(value.doubleValue());
            case Float ignored -> Float.toString(value.floatValue());
            case Long ignored -> Long.toString(value.longValue());
            case Integer ignored -> Integer.toString(value.intValue());
            case Short ignored -> Short.toString(value.shortValue());
            case Byte ignored -> Byte.toString(value.byteValue());
            default -> value.toString();
        };
    }

    @SuppressWarnings("unchecked")
    @Override
    public <T> T toObject(Class<T> clazz, Class<?> parent) {
        if (clazz.equals(Double.class) || clazz.equals(double.class)) {
            return (T) (Double) value.doubleValue();
        } else if (clazz.equals(Float.class) || clazz.equals(float.class)) {
            return (T) (Float) value.floatValue();
        } else if (clazz.equals(Long.class) || clazz.equals(long.class)) {
            return (T) (Long) value.longValue();
        } else if (clazz.equals(Integer.class) || clazz.equals(int.class)) {
            return (T) (Integer) value.intValue();
        } else if (clazz.equals(Short.class) || clazz.equals(short.class)) {
            return (T) (Short) value.shortValue();
        } else if (clazz.equals(Byte.class) || clazz.equals(byte.class)) {
            return (T) (Byte) value.byteValue();
        } else {
            throw new RuntimeException("Cannot convert JsonNumber to '" + clazz.getName() + "'");
        }
    }
}