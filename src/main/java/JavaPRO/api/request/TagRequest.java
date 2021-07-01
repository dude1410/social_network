package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
@NoArgsConstructor
public class TagRequest {

    @Schema(name = "tag", description = "текст тэга", example = "музыка")
    String tag;
}
