package JavaPRO.api.request;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class MailSupportRequest {

    private String email;
    private String text;
}
