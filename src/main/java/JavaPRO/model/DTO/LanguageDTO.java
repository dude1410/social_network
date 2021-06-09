package JavaPRO.model.DTO;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class LanguageDTO {

    private int id;
    private JavaPRO.model.ENUM.Language title;
}
