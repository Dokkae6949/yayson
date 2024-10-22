package at.kaindorf.pojos;

import at.kaindorf.annotations.JsonIgnore;
import at.kaindorf.annotations.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {
    private int id;
    @JsonProperty("firstname")
    private String firstName;
    @JsonProperty("lastname")
    private String lastName;
    @JsonIgnore
    private int age;
}
