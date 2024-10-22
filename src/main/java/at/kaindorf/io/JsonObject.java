package at.kaindorf.io;

import at.kaindorf.annotations.JsonIgnore;
import at.kaindorf.annotations.JsonIgnoreProperties;
import at.kaindorf.annotations.JsonProperty;
import lombok.ToString;

import java.lang.reflect.Field;
import java.util.LinkedHashMap;
import java.util.Map;

@ToString
public class JsonObject extends JsonValue {
    private final Map<String, JsonValue> values = new LinkedHashMap<>();

    public JsonObject(Map<String, JsonValue> values) {
        this.values.putAll(values);
    }

    public JsonObject(Object object) {
        Field[] fields = object.getClass().getDeclaredFields();

        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }

            field.setAccessible(true);

            String fieldName = getJsonFieldName(field);
            Object fieldValue;

            try {
                fieldValue = field.get(object);
            } catch (IllegalAccessException e) {
                throw new RuntimeException(String.format("Could not access field %s of object %s", fieldName, object), e);
            }

            values.put(fieldName, JsonValue.fromObject(fieldValue, object.getClass()));
        }
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        String newLine = getJsonStyle().isObjectMultiline() ? "\n" : "";
        String indentation = " ".repeat(getJsonStyle().getIndentation());

        sb.append("{").append(newLine);

        for (Map.Entry<String, JsonValue> entry : values.entrySet()) {
            String value = entry
                    .getValue()
                    .setJsonStyle(getJsonStyle())
                    .toJson()
                    .replaceAll("\n", "\n" + indentation);

            sb.append(indentation).append("\"").append(entry.getKey()).append("\"");
            sb.append(getJsonStyle().isSpaceBeforeColon() ? " " : "").append(":").append(getJsonStyle().isSpaceAfterColon() ? " " : "");
            sb.append(value).append(",").append(newLine);
        }

        int lastCommaIndex = sb.lastIndexOf(",");
        if (lastCommaIndex >= 0)
            sb.deleteCharAt(lastCommaIndex);
        sb.append("}");

        return sb.toString();
    }

    @Override
    public <T> T toObject(Class<T> clazz, Class<?> parent) {
        Field[] fields = clazz.getDeclaredFields();
        T instance;

        try {
            instance = clazz.getDeclaredConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("Cannot instantiate object of class " + clazz.getName());
        }

        for (Field field : fields) {
            if (field.isAnnotationPresent(JsonIgnore.class)) {
                continue;
            }

            field.setAccessible(true);

            String name = getJsonFieldName(field);
            JsonValue value = values.get(name);

            if (value == null) {
                if (clazz.isAnnotationPresent(JsonIgnoreProperties.class)
                        && clazz.getAnnotation(JsonIgnoreProperties.class).ignoreUnknown()) {
                    continue;
                }

                throw new RuntimeException("Field '" + name + "' required for '" + clazz.getSimpleName() + "' but not found in JSON object");
            }

            try {
                field.set(instance, value.toObject(field.getType(), clazz));
            } catch (IllegalAccessException e) {
                throw new RuntimeException("Cannot set field '" + name + "' for '" + clazz.getSimpleName() + "'");
            }
        }

        return instance;
    }

    /**
     * Returns the JSON field name for a given field.
     * If the field is annotated with {@link JsonProperty}, the value of the annotation is returned.
     * Otherwise, the field name is returned.
     * @param field the field to get the JSON field name for
     * @return the JSON field name
     */
    private static String getJsonFieldName(Field field) {
        if (field.isAnnotationPresent(JsonProperty.class)
                && !field.getAnnotation(JsonProperty.class).value().isEmpty()) {
            return field.getAnnotation(JsonProperty.class).value();
        }

        return field.getName();
    }
}
