package JavaPRO.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse {

    @JsonProperty(value = "error")
    private String error;
    @JsonProperty(value = "error_description")
    private String errorDescription;


}
