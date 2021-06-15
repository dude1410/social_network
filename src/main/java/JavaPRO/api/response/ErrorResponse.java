package JavaPRO.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ErrorResponse implements Response{

    @Schema(description = "ошибка", example = "invalid password")
    private String error;

    @JsonProperty(value = "error_description")
    @Schema(description = "описание ошибки", example = "вы ввели неверный пароль")
    private String errorDescription;
}
