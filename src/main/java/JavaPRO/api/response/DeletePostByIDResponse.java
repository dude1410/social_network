package JavaPRO.api.response;

import JavaPRO.model.DTO.PostDeleteDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class DeletePostByIDResponse extends Response {

    @Schema(description = "ошибка", example = "invalid_request")
    private String error;

    @Schema(description = "метка времени в формате long", example = "2147483648L")
    private Long timestamp;

    @Schema(description = "данные об удаленном посте")
    private PostDeleteDTO data;
}
