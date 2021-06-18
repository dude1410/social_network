package JavaPRO.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ResponseData {

    @Schema(description = "сообщение", example = "invalid_request")
    private String message;
}
