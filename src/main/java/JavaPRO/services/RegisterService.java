package JavaPRO.services;

import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.RegisterResponse;
import JavaPRO.api.response.RegisterResponseData;
import JavaPRO.config.MailConfig;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import java.text.SimpleDateFormat;
import java.util.Date;

@Service
public class RegisterService {

    @Autowired
    JavaMailSender javaMailSender;

    @Autowired
    MailConfig mailConfig;

    public RegisterResponse registerNewUser(RegisterRequest userInfo){

        if (userFindInDB(userInfo.getEmail())){
            return new RegisterResponse("registration error",
                    getTimestamp(), new RegisterResponseData("the user was previously registered"));
        }
        else {
            addUserInDB(userInfo);
            sendRegistryMail(userInfo.getEmail());
            return new RegisterResponse("null", getTimestamp(), new RegisterResponseData("OK"));
        }
    }

    // TODO: 20.05.2021
    private boolean userFindInDB(String email){
        return false;
    }

    // TODO: 23.05.2021
    private void addUserInDB(RegisterRequest userInfo){}

    private void sendRegistryMail(String email){
        final SimpleMailMessage simpleMail = new SimpleMailMessage();
        simpleMail.setFrom(mailConfig.getUsername());
        simpleMail.setTo(email);
        simpleMail.setSubject("Registration in Developers social network");
        simpleMail.setText("Hello, you have been successfully registered in Developers social network");
        javaMailSender.send(simpleMail);
    }

    private Long getTimestamp(){
        return Long.parseLong(new SimpleDateFormat("ddMMyyHHmmSS").format(new Date()));
    }

}
