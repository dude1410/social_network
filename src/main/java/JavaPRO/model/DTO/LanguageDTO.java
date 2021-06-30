package JavaPRO.model.DTO;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {

    @Schema(description = "id языка", example = "15")
    private int id;

    @Schema(description = "язык", example = "русский")
    private String title;
}
