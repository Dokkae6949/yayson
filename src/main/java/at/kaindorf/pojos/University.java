package at.kaindorf.pojos;

import at.kaindorf.annotations.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class University {
    @JsonProperty("name")
    private String universityName;
    private Address address;
    private List<Department> departments;
}
