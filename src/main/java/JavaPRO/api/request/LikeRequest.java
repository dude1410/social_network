package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class LikeRequest {
    @Schema(name = "item_id", description = "item_id", example = "12")
    Integer item_id;

    @Schema(name = "type", description = "Объект, на который ставится лайк", example = "Comment или Post")
    String type;
}
