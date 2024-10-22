package at.kaindorf.io;

import at.kaindorf.annotations.JsonFormat;
import lombok.Getter;
import lombok.NonNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

@Getter
public abstract class JsonValue {
    @NonNull
    private JsonStyle jsonStyle = JsonStyle.compact();

    /**
     * Sets the style of the JSON
     * @param jsonStyle the style to set
     * @return the JsonValue
     */
    public JsonValue setJsonStyle(@NonNull JsonStyle jsonStyle) {
        this.jsonStyle = jsonStyle;
        return this;
    }

    /**
     * Converts the JsonValue to a JSON string
     * @return the JSON string
     */
    public abstract String toJson();

    /**
     * Converts the JsonValue to a java object
     * @param clazz the class to convert to
     * @param parent the parent the object is on or null if it does not have one
     * @return the object
     * @param <T> the type of the object
     */
    public abstract <T> T toObject(Class<T> clazz, Class<?> parent);

    /**
     * Converts an object to a JsonValue
     * @param object the object to convert
     * @param parent the parent the object is on or null if it does not have one
     * @return the JsonValue
     */
    public static JsonValue fromObject(Object object, Class<?> parent) {
        if (object != null && object.getClass().isArray()) {
            return new JsonArray(object);
        }

        return switch (object) {
            case null -> new JsonNull();
            case Boolean bool -> new JsonBoolean(bool);
            case Number number -> new JsonNumber(number);
            case String string -> new JsonString(string);
            case LocalDate date -> {
                if (parent == null || !parent.isAnnotationPresent(JsonFormat.class)) {
                    yield new JsonString(date.toString());
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(parent.getDeclaredAnnotation(JsonFormat.class).value());
                yield new JsonString(date.format(dtf));
            }
            case LocalTime time -> {
                if (parent == null || !parent.isAnnotationPresent(JsonFormat.class)) {
                    yield new JsonString(time.toString());
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(parent.getDeclaredAnnotation(JsonFormat.class).value());
                yield new JsonString(time.format(dtf));
            }
            case LocalDateTime dateTime -> {
                if (parent == null || !parent.isAnnotationPresent(JsonFormat.class)) {
                    yield new JsonString(dateTime.toString());
                }

                DateTimeFormatter dtf = DateTimeFormatter.ofPattern(parent.getDeclaredAnnotation(JsonFormat.class).value());
                yield new JsonString(dateTime.format(dtf));
            }
            case JsonValue jsonValue -> jsonValue;
            case Iterable<?> iterable -> new JsonArray(iterable);
            default -> new JsonObject(object);
        };
    }
}