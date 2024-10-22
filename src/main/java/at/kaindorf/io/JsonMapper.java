package at.kaindorf.io;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.NonNull;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JsonMapper {
    @NonNull
    private JsonStyle format = JsonStyle.compact();

    /**
     * Serializes an object to a JSON string.
     * @param obj the object to serialize
     * @return the JSON string
     */
    public String serialize(Object obj) {
        return JsonValue.fromObject(obj, null).setJsonStyle(format).toJson();
    }

    /**
     * Serializes an object to a JSON string and writes it to a file.
     * @param obj the object to serialize
     * @param path the path to write the JSON string to
     * @throws IOException if an I/O error occurs
     */
    public void serialize(Object obj, Path path) throws IOException {
        Files.writeString(path, serialize(obj));
    }

    /**
     * Deserializes a JSON string to an object of the given class.
     * @param json the JSON string to deserialize
     * @param clazz the class of the object to deserialize to
     * @return the deserialized object
     * @param <T> the type of the object to deserialize
     */
    public <T> T deserialize(String json, Class<T> clazz) {
        return new JsonParser(json).parse().toObject(clazz, null);
    }

    /**
     * Deserializes a JSON string from a file to an object of the given class.
     * @param path the path to read the JSON string from
     * @param clazz the class of the object to deserialize to
     * @return the deserialized object
     * @throws IOException if an I/O error occurs
     * @param <T> the type of the object to deserialize
     */
    public <T> T deserialize(Path path, Class<T> clazz) throws IOException {
        return deserialize(Files.readString(path), clazz);
    }
}
