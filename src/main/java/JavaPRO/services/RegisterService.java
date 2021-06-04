package JavaPRO.services;

import JavaPRO.api.request.RegisterConfirmRequest;
import JavaPRO.api.request.RegisterRequest;
import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.Response;
import JavaPRO.api.response.ResponseData;
import JavaPRO.model.ENUM.MessagesPermission;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class RegisterService {

    @Autowired
    PersonRepository personRepository;

    @Autowired
    EmailService emailService;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Value("${spring.mail.address}")
    private String address;

    public ResponseEntity<Response> registerNewUser(RegisterRequest userInfo){
        if (userFindInDB(userInfo.getEmail())){
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "user previously registered"), HttpStatus.BAD_REQUEST);
        }
        else {
            String token = getToken();
            int newUserId = addUserInDB(userInfo, token);
            String messageBody = "Hello, to complete the registration follow to link " +
                                "<a href=\"" + address + "/registration/complete?userId=" +
                                newUserId + "&token=" + token + "\">Confirm registration</a>";
            emailService.sendMail("Registration in social network", messageBody, userInfo.getEmail());
            return new ResponseEntity<>(new OkResponse("null", getTimestamp().longValue(), new ResponseData("OK")), HttpStatus.OK);
        }
    }

    public ResponseEntity<Response> confirmRegistration(RegisterConfirmRequest registerConfirmRequest){
        Integer userId = registerConfirmRequest.getUserId();
        String token = registerConfirmRequest.getToken();
        Person person = personRepository.findByIdAndCode(userId, token);
        if (personRepository.findByIdAndCode(userId, token) != null && !person.isApproved()) {
            if (personRepository.setIsApprovedTrue(userId) != null) {
                return new ResponseEntity<>(new OkResponse("null", getTimestamp().longValue(), new ResponseData("OK")), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("invalid_request", "confirm registration error"), HttpStatus.BAD_REQUEST);

            }
        }
        else {
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "confirm registration error"), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean userFindInDB(String email){
        return personRepository.findByEmail(email) != null;
    }

    private int addUserInDB(RegisterRequest userInfo, String token){
        Person person = new Person();
        person.setFirstName(userInfo.getFirstName());
        person.setLastName(userInfo.getLastName());
        person.setPassword(passwordEncoder.encode(userInfo.getPasswd1()));
        person.setEmail(userInfo.getEmail());
        person.setRegDate(new Timestamp(System.currentTimeMillis()));
        person.setApproved(false);
        person.setMessagesPermission(MessagesPermission.ALL);
        person.setRole(0);
        person.setLastOnlineTime(new Timestamp(System.currentTimeMillis()));
        person.setConfirmationCode(token);
        personRepository.save(person);
        return person.getId();
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
        return passwordEncoder.encode(token.toString());
    }
}
