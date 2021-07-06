package javapro.api.response;

import javapro.model.dto.PostDeleteDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletePostByIDResponse {

    @Schema(description = "ошибка", example = "invalid_request")
    private String error;

    @Schema(description = "метка времени в формате long", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "объект PostDeleteDTO")
    private PostDeleteDTO data;
}
