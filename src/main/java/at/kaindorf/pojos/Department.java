package at.kaindorf.pojos;

import at.kaindorf.annotations.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Department {
    @JsonProperty("id")
    private int departmentId;
    @JsonProperty("name")
    private String departmentName;
    @JsonProperty("head")
    private HeadOfDepartment headOfDepartment;
    private List<Professor> profs;
}
