package javapro.services;

import javapro.api.request.MailSupportRequest;
import javapro.api.response.MailSupportResponse;
import javapro.config.Config;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;


@Service
public class EmailService {

    private final JavaMailSender javaMailSender;
    private final Logger logger;
    @Value("${spring.mail.username}")
    private String username;

    public EmailService(JavaMailSender javaMailSender,
                        @Qualifier("mailSenderLogger") Logger logger) {
        this.javaMailSender = javaMailSender;
        this.logger = logger;
    }

    public void sendMail(String subject, String messageBody, String email){
        new Thread(() -> {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                logger.info(String.format("Начало отправки сообщения '%s' на почту %s.", subject, email));
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true, "UTF-8");
                messageHelper.setFrom(username);
                messageHelper.setTo(email);
                messageHelper.setSubject(subject);
                message.setContent(messageBody, "text/html; charset=UTF-8");
                javaMailSender.send(message);
            } catch (MessagingException e) {
                logger.error(e.toString());
                logger.error(String.format("Ошибка отправки сообщения '%s' на почту %s.", subject, email));
            }
            logger.info(String.format("Успешная отправка сообщения '%s' на почту %s.", subject, email));
        }).start();
    }

    public ResponseEntity<MailSupportResponse> sendMailToSupport(MailSupportRequest mailSupportRequest) {
        sendMail((Config.STRING_MAIL_TO_SUPPORT_SUBJECT + mailSupportRequest.getEmail()),
                mailSupportRequest.getText(),
                username);
        return ResponseEntity
                .ok(new MailSupportResponse(true, Config.STRING_MAIL_TO_SUPPORT_RESPONSE));
    }
}
