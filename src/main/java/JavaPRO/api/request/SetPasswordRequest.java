package JavaPRO.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;

import javax.validation.constraints.NotBlank;

@Data
@NoArgsConstructor
public class SetPasswordRequest {

    @NotNull
    @NotBlank
    String token;

    @NotNull
    @NotBlank
    String password;
}
