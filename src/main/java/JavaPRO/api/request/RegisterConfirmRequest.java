package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class RegisterConfirmRequest {

    private Integer userId;

    private String token;

}
