package at.kaindorf.io;

import lombok.ToString;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;

@ToString
public class JsonArray extends JsonValue {
    private final List<JsonValue> values = new ArrayList<>();

    public JsonArray(List<JsonValue> list) {
        values.addAll(list);
    }

    public JsonArray(Object object) {
        if (object.getClass().isArray()) {
            for (int i = 0; i < Array.getLength(object); i++) {
                values.add(JsonValue.fromObject(Array.get(object, i), object.getClass()));
            }
        } else if (object instanceof Iterable) {
            for (Object element : (Iterable<?>) object) {
                values.add(JsonValue.fromObject(element, object.getClass()));
            }
        }
    }

    @Override
    public String toJson() {
        StringBuilder sb = new StringBuilder();
        String newLine = getJsonStyle().isArrayMultiline() ? "\n" : "";
        String indentation = getJsonStyle().isArrayMultiline() ? " ".repeat(getJsonStyle().getIndentation()) : "";
        String commaSpacing = !getJsonStyle().isArrayMultiline() && getJsonStyle().isSpaceAfterComma() ? " " : "";

        sb.append("[").append(newLine);

        for (JsonValue element : values) {
            String value = element
                    .setJsonStyle(getJsonStyle())
                    .toJson()
                    .replaceAll("\n", "\n" + indentation);

            sb.append(indentation).append(value).append(",").append(commaSpacing).append(newLine);
        }

        int lastCommaIndex = sb.lastIndexOf(",");
        if (lastCommaIndex >= 0)
            sb.deleteCharAt(lastCommaIndex);

        int lastSpaceIndex = sb.lastIndexOf(" ");
        if (!getJsonStyle().isArrayMultiline() && lastSpaceIndex >= 0)
            sb.deleteCharAt(lastSpaceIndex);

        sb.append("]");

        return sb.toString();
    }

    @Override
    public <T> T toObject(Class<T> clazz, Class<?> parent) {
        if (!clazz.equals(List.class) && !clazz.isArray()) {
            throw new RuntimeException("Cannot convert JsonArray to '" + clazz.getName() + "'");
        }

        if (clazz.isArray()) {
            Class<?> componentType = clazz.getComponentType();
            Object array = Array.newInstance(componentType, values.size());

            for (int i = 0; i < values.size(); i++) {
                Array.set(array, i, values.get(i).toObject(componentType, parent));
            }

            return clazz.cast(array);
        } else {
            return clazz.cast(values);
        }
    }
}
