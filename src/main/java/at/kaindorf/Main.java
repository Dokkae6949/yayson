package at.kaindorf;

import at.kaindorf.io.JsonStyle;
import at.kaindorf.io.JsonMapper;
import at.kaindorf.pojos.University;

import java.io.IOException;
import java.nio.file.Path;

public class Main {
    public static void main(String[] args) throws IOException {
        Path path = Path.of("src", "main", "resources", "university.json");
        JsonMapper mapper = new JsonMapper(JsonStyle.ugly());
        University university = mapper.deserialize(path, University.class);
        university.setUniversityName("University of Graz");
        university.getAddress().setCity("Graz");
        mapper.serialize(university, path);
    }
}