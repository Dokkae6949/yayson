package at.kaindorf.pojos;

import at.kaindorf.annotations.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Address {
    @JsonProperty("streetName")
    private String street;
    @JsonProperty("streetNum")
    private String number;
    private String plz;
    private String city;
}
