package JavaPRO.services;

import JavaPRO.api.request.MailSupportRequest;
import JavaPRO.api.response.MailSupportResponse;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.util.regex.Pattern;


@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Logger logger;

    @Value("${spring.mail.username}")
    private String username;

    public static final Pattern VALID_EMAIL_ADDRESS_REGEX =
            Pattern.compile("^[A-Z0-9._%+-]+@[A-Z0-9.-]+\\.[A-Z]{2,6}$", Pattern.CASE_INSENSITIVE);

    public EmailService(JavaMailSender javaMailSender,
                        @Qualifier("mailSenderLogger") Logger logger) {
        this.javaMailSender = javaMailSender;
        this.logger = logger;
    }

    public void sendMail(String subject, String messageBody, String email){
        new Thread(() -> {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                logger.info(String.format("Начало отправки сообщения '%s' на почту '%s'.", subject, email));
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
                messageHelper.setFrom(username);
                messageHelper.setTo(email);
                messageHelper.setSubject(subject);
                message.setContent(messageBody, "text/html; charset=UTF-8");
                javaMailSender.send(message);
            } catch (MessagingException e){
                e.printStackTrace();
                logger.error(String.format("Ошибка отправки сообщения '%s' на почту '%s'.", subject, email));
            }
            logger.info(String.format("Успешная отрпавка сообщения '%s' на почту '%s'.", subject, email));
        }).start();
    }

    public ResponseEntity<MailSupportResponse> sendMailToSupport(MailSupportRequest mailSupportRequest) throws BadRequestException {
        if (mailSupportRequest.getEmail() == null) {
            logger.error("Ошибка отправки сообщения в поддержу. Неверный формат запроса");
            throw new BadRequestException(Config.STRING_MAIL_TO_SUPPORT_NO_EMAIL);
        }
        if (!VALID_EMAIL_ADDRESS_REGEX.matcher(mailSupportRequest.getEmail()).matches()){
            logger.error("Ошибка отправки сообщения в поддержу. Неверный формат запроса");
            throw new BadRequestException(Config.STRING_MAIL_TO_SUPPORT_NO_EMAIL);
        }
        if (mailSupportRequest.getText() == null) {
            logger.error("Ошибка отправки сообщения в поддержу. Неверный формат запроса");
            throw new BadRequestException(Config.STRING_MAIL_TO_SUPPORT_NO_TEXT);
        }
        if (mailSupportRequest.getText().length() < 20) {
            logger.error("Ошибка отправки сообщения в поддержу. Неверный формат запроса");
            throw new BadRequestException(Config.STRING_MAIL_TO_SUPPORT_NO_TEXT);
        }
        sendMail((Config.STRING_MAIL_TO_SUPPORT_SUBJECT + mailSupportRequest.getEmail()),
                mailSupportRequest.getText(),
                username);
        return ResponseEntity
                .ok(new MailSupportResponse(true, Config.STRING_MAIL_TO_SUPPORT_RESPONSE));
    }
}
