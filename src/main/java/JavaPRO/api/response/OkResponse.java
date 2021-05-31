package JavaPRO.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class OkResponse implements Response{

    @JsonProperty
    private String error;
    @JsonProperty
    private Long timestamp;
    @JsonProperty
    private ResponseData data;

}
