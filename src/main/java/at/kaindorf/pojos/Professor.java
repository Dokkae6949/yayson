package at.kaindorf.pojos;

import at.kaindorf.annotations.JsonFormat;
import at.kaindorf.annotations.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Professor {
    @JsonProperty("id")
    private int profId;
    @JsonProperty("fname")
    private String firstName;
    @JsonProperty("lname")
    private String lastName;
    @JsonProperty("hdate")
    @JsonFormat("MM/dd/yyyy")
    private LocalDate hireDate;
    private List<Course> courses;
}
