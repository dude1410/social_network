package JavaPRO.api.response;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.HashMap;
import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsFriendResponse {

    @Schema(description = "ответ по статусу дружбы")
    List<IsOneFriendResponse> data;
}
