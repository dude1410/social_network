package JavaPRO.services;

import JavaPRO.api.request.MailSupportRequest;
import JavaPRO.api.response.*;
import JavaPRO.config.Config;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    @Value("${spring.mail.username}")
    private String username;

    public void sendMail(String subject, String messageBody, String email){
        new Thread(() -> {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8"); // todo: change
                messageHelper.setFrom(username);
                messageHelper.setTo(email);
                messageHelper.setSubject(subject);
                message.setContent(messageBody, "text/html");
                javaMailSender.send(message);
            } catch (MessagingException e){
                e.printStackTrace();
            }
        }).start();
    }

    public ResponseEntity<Response> sendMailToSupport(MailSupportRequest mailSupportRequest){
        if (mailSupportRequest.getEmail() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MailSupportResponse(false, Config.STRING_MAIL_TO_SUPPORT_NO_EMAIL));
        }
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(mailSupportRequest.getEmail()).matches()){
            return ResponseEntity
                    .badRequest()
                    .body(new MailSupportResponse(false, Config.STRING_MAIL_TO_SUPPORT_NO_EMAIL));
        }
        if (mailSupportRequest.getText() == null) {
            return ResponseEntity
                    .badRequest()
                    .body(new MailSupportResponse(false, Config.STRING_MAIL_TO_SUPPORT_NO_TEXT));
        }
        if (mailSupportRequest.getText().length() < 20) {
            return ResponseEntity
                    .badRequest()
                    .body(new MailSupportResponse(false, Config.STRING_MAIL_TO_SUPPORT_NO_TEXT));
        }
        System.out.println(mailSupportRequest.getText());
       sendMail((Config.STRING_MAIL_TO_SUPPORT_SUBJECT + mailSupportRequest.getEmail()),
               mailSupportRequest.getText(),
               mailSupportRequest.getEmail()); // todo: "request.getEmail" change to "username"
        return ResponseEntity
                .ok(new MailSupportResponse(true, Config.STRING_MAIL_TO_SUPPORT_RESPONSE));
    }
}
