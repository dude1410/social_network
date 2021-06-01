package JavaPRO.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class EmailService {

    @Autowired
    JavaMailSender javaMailSender;

    @Value("${spring.mail.username}")
    private String username;

    public void sendMail(String subject, String messageBody, String email){
        new Thread(() -> {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
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
}
