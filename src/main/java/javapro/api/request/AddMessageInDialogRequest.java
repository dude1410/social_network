package javapro.api.request;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AddMessageInDialogRequest {

    @NotNull
    String message;
}
