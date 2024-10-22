package at.kaindorf.io;

import at.kaindorf.annotations.JsonFormat;
import lombok.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@ToString
@AllArgsConstructor
public class JsonString extends JsonValue {
    @NonNull
    private final String value;

    @Override
    public String toJson() {
        return "\"" + value + "\"";
    }

    @Override
    public <T> T toObject(Class<T> clazz, Class<?> parent) {
        if (clazz.equals(String.class)) {
            return clazz.cast(value);
        }

        if (clazz.equals(LocalDate.class)) {
            if (parent == null || !parent.isAnnotationPresent(JsonFormat.class)) {
                return (T) LocalDate.parse(value);
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(parent.getDeclaredAnnotation(JsonFormat.class).value());
            return (T) LocalDate.parse(value, dtf);
        }

        if (clazz.equals(LocalTime.class)) {
            if (parent == null || !parent.isAnnotationPresent(JsonFormat.class)) {
                return (T) LocalTime.parse(value);
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(parent.getDeclaredAnnotation(JsonFormat.class).value());
            return (T) LocalTime.parse(value, dtf);
        }

        if (clazz.equals(LocalDateTime.class)) {
            if (parent == null || !parent.isAnnotationPresent(JsonFormat.class)) {
                return (T) LocalDateTime.parse(value);
            }

            DateTimeFormatter dtf = DateTimeFormatter.ofPattern(parent.getDeclaredAnnotation(JsonFormat.class).value());
            return (T) LocalDateTime.parse(value, dtf);
        }

        throw new RuntimeException("Cannot convert JsonString to '" + clazz.getName() + "'");
    }
}
