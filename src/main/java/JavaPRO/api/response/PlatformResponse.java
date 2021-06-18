package JavaPRO.api.response;

import JavaPRO.model.DTO.LanguageDTO;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PlatformResponse {

  private String error;
  private Long timestamp;
  private int total;
  private int offset;
  private int perPage;
  private LanguageDTO data;

}
