package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class DeletePostByIDRequest {

    @Schema(description = "id поста", example = "12")
    private Integer id;
}
