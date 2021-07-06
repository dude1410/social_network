package javapro.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class TagRequest {

    @Schema(name = "tag", description = "текст тэга", example = "музыка")
    String tag;
}
