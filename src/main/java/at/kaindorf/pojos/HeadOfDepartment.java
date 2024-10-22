package at.kaindorf.pojos;

import at.kaindorf.annotations.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class HeadOfDepartment {
    @JsonProperty("name")
    private String firstName;
    private String lastName;
    private Title title;
}
