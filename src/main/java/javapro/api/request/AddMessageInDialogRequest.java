package javapro.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.validation.constraints.NotNull;

@Data
@NoArgsConstructor
public class AddMessageInDialogRequest {

    @NotNull
    @JsonProperty("message_text")
    String message;
}
