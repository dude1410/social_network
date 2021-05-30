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


    public void sendRegistryMail(int id, String token, String email){
        new Thread(() -> {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                String str = "Hello, to complete the registration, " + "follow to link " +
                        "<a href=\"http://localhost:8080/registration/complete?userId=" + id + "&token=" + token + "\">Confirm registration</a>";
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setFrom(username);
                messageHelper.setTo(email);
                messageHelper.setSubject("Registration in Developers social network");
                message.setContent(str, "text/html");
                javaMailSender.send(message);
            } catch (MessagingException e){
                e.printStackTrace();
            }
        }).start();
    }

    public void sendRecoveryMail(String token, String email){
        new Thread(() -> {
            MimeMessage message = javaMailSender.createMimeMessage();
            try {
                String str = "Hello, to recovery your password follow to link " +
                        "<a href=\"http://localhost:8080/change-password?token=" + token + "\">Password recovery</a>";
                MimeMessageHelper messageHelper = new MimeMessageHelper(message, true);
                messageHelper.setFrom(username);
                messageHelper.setTo(email);
                messageHelper.setSubject("Password recovery in Developers social network");
                message.setContent(str, "text/html");
                javaMailSender.send(message);
            } catch (MessagingException e){
                e.printStackTrace();
            }
        }).start();
    }
}
