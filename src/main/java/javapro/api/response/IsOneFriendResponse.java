package javapro.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsOneFriendResponse {

    @JsonProperty("user_id")
    @Schema(description = "id пользователя", example = "15")
    private Integer userId;

    @Schema(description = "статус дружбы", example = "FRIEND")
    private String status;
}
