package JavaPRO.api.request;

import lombok.Data;

@Data
public class RegisterConfirmRequest {

    private Integer userId;

    private String token;

}
