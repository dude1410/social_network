package JavaPRO.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class OnlyMailRequest {

    @JsonProperty
    private String email;


}
