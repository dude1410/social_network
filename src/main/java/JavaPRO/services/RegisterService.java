package JavaPRO.services;

import JavaPRO.api.request.RegisterConfirmRequest;
import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.MailConfig;
import JavaPRO.model.ENUM.MessagesPermission;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
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
    PersonRepository personRepository;

    @Autowired
    MailConfig mailConfig;

    public OkResponse registerNewUser(RegisterRequest userInfo){
        if (userFindInDB(userInfo.getEmail())){
            return new OkResponse("registration error",
                    getTimestamp(), new ResponseData("the user was previously registered"));
        }
        else {
            String token = getToken();
            int newUserId = addUserInDB(userInfo, token);
            sendRegistryMail(newUserId, token, userInfo.getEmail());
            return new OkResponse("null", getTimestamp(), new ResponseData("OK"));
        }
    }

    public OkResponse confirmRegistration(RegisterConfirmRequest registerConfirmRequest){
        Integer userId = registerConfirmRequest.getUserId();
        String token = registerConfirmRequest.getToken();
        if (personRepository.findByIdAndCode(userId, token) != null) {
            personRepository.setIsApprovedTrue(userId);
            return new OkResponse("null", getTimestamp(), new ResponseData("OK"));
        }
        else {
            return new OkResponse("confirm error",
                    getTimestamp(), new ResponseData("error on confirm registration: user not found"));
        }
    }

    // TODO: 20.05.2021
    private boolean userFindInDB(String email){
        return personRepository.findByEmail(email) != null;
    }

    // TODO: 23.05.2021
    private int addUserInDB(RegisterRequest userInfo, String token){
        Person person = new Person();
        person.setFirstName(userInfo.getFirstName());
        person.setLastName(userInfo.getLastName());
        person.setPassword(userInfo.getPasswd1());
        person.setEmail(userInfo.getEmail());
        person.setRegDate(new Date());
        person.setApproved(false);
        person.setMessagesPermission(MessagesPermission.ALL);
        //уточнить
        person.setRole(0);
        //уточнить
        person.setLastOnlineTime(new Date());
        person.setConfirmationCode(token);
        personRepository.save(person);
        return person.getId();
    }

    private void sendRegistryMail(int id, String token, String email){
        new Thread(() -> {
            final SimpleMailMessage simpleMail = new SimpleMailMessage();
            simpleMail.setFrom(mailConfig.getUsername());
            simpleMail.setTo(email);
            simpleMail.setSubject("Registration in Developers social network");
            simpleMail.setText("Hello, to complete the registration, " + "follow to link " +
                    "http://localhost:8080/registration/complete?userId=" + id + "&token=" + token);
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
