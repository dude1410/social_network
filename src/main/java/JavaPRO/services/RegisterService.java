package JavaPRO.services;

import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.util.Date;

@Service
public class RegisterService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    MailConfig mailConfig;

    public OkResponse registerNewUser(RegisterRequest userInfo){

        if (userFindInDB(userInfo.getEmail())){
            return new OkResponse("registration error",
                    getTimestamp(), new ResponseData("the user was previously registered"));
        }
        else {
            addUserInDB(userInfo);
            sendRegistryMail(userInfo.getEmail());
            return new OkResponse("null", getTimestamp(), new ResponseData("OK"));
        }
    }

    // TODO: 20.05.2021
    private boolean userFindInDB(String email){
        return false;
    }

    // TODO: 23.05.2021
    private void addUserInDB(RegisterRequest userInfo){}

    private void sendRegistryMail(String email){
        new Thread(() -> {
            final SimpleMailMessage simpleMail = new SimpleMailMessage();
            simpleMail.setFrom(mailConfig.getUsername());
            simpleMail.setTo(email);
            simpleMail.setSubject("Registration in Developers social network");
            simpleMail.setText("Hello, to complete the registration, " +
                    "follow to link http://localhost:8080/api/v1/account/register/complete?userId=1&token="
                    + getToken());
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
        return sha256(token.toString());
    }

    public static String sha256(String token) {
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(token.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder();

            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }

            return hexString.toString();
        } catch(Exception ex){
            throw new RuntimeException(ex);
        }
    }
}
