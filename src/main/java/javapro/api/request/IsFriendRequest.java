package javapro.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class IsFriendRequest {

    @JsonProperty("user_ids")
    @Schema(description = "список id пользователей для проверки", example = "[1 , 10, 12]")
    List<Integer> userIds;
}
