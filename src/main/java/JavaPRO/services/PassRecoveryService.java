package JavaPRO.services;

import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class PassRecoveryService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    MailConfig mailConfig;

    public OkResponse passRecovery(String email){
        if (userFindInDB(email)) {
            sendRecoveryMail(email);
            return new OkResponse("null",
                    getTimestamp(), new ResponseData("OK"));
        }
        else {
            return new OkResponse("password recovery error",
                    getTimestamp(), new ResponseData("the user not found in database"));
        }
    }

    // TODO: 27.05.2021
    private boolean userFindInDB(String email){
        return true;
    }

    private void sendRecoveryMail(String email){
        new Thread(() -> {
            final SimpleMailMessage simpleMail = new SimpleMailMessage();
            simpleMail.setFrom(mailConfig.getUsername());
            simpleMail.setTo(email);
            simpleMail.setSubject("Recovery password in Developers social network");
            simpleMail.setText("Hello, to recover your password follow to link http://localhost:8080/password/reset/" + getToken());
            javaMailSender.send(simpleMail);
        }).start();
    }

    private Long getTimestamp(){
        return (new Date().getTime() / 1000);
    }

    private String getToken(){
        StringBuilder token = new StringBuilder();
        String strMass = "QWERTYUIOPASDFGHJKLZXCVBNM1234567890";
        for (int i = 0; i < 10; i++){
            token.append(strMass.charAt((int) (Math.random() * (strMass.length()))));
        }
        return token.toString();
    }
}
