package JavaPRO.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailSupportResponse implements Response {

    private boolean sent;
    private String text;
}
