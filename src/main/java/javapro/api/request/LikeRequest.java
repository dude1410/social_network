package javapro.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class LikeRequest {

    @JsonProperty(value = "item_id")
    @Schema(name = "item_id", description = "item_id", example = "12")
    Integer itemID;

    @Schema(name = "type", description = "Объект, на который ставится лайк", example = "Comment или Post")
    String type;
}
