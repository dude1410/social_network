package JavaPRO.api.request;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class SetPasswordRequest {

    @Schema(description = "токен для восстановления пароля", example = "$2a$12$PnIJuvWmVLBiWuKIdzNI6eyMpdW932zZ4XDsmWVsK8K0quBg5CemK")
    String token;

    @Schema(description = "новый пароль", example = "Ss123456")
    String password;
}
