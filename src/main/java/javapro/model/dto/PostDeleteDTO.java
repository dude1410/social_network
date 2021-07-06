package javapro.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

@Data
public class PostDeleteDTO {

    @JsonProperty(value = "id")
    private Integer id;
}
