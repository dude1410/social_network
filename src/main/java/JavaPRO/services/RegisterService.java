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
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.Date;

@Service
public class RegisterService {

    private final PersonRepository personRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;
    private final TokenService tokenService;

    @Value("${spring.mail.address}")
    private String address;

    public RegisterService(PersonRepository personRepository,
                           EmailService emailService,
                           PasswordEncoder passwordEncoder,
                           TokenService tokenService) {
        this.personRepository = personRepository;
        this.emailService = emailService;
        this.passwordEncoder = passwordEncoder;
        this.tokenService = tokenService;
    }

    public ResponseEntity<Response> registerNewUser(RegisterRequest userInfo){
        if (userFindInDB(userInfo.getEmail())){
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "user previously registered"), HttpStatus.BAD_REQUEST);
        }
        else {
            String token = tokenService.getToken();
            if (addUserInDB(userInfo, token) != null) {
                emailService.sendMail("Registration in social network", getConfirmMessage(token), userInfo.getEmail());
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("invalid_request", "user not add"), HttpStatus.BAD_REQUEST);
            }
        }
    }

    public ResponseEntity<Response> confirmRegistration(RegisterConfirmRequest registerConfirmRequest) {
        String token = registerConfirmRequest.getToken();
        Person person = personRepository.findByConfirmationCode(token);
        if (person != null && !person.isApproved() && tokenService.checkToken(token)) {
            person.setApproved(true);
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "confirm registration error"), HttpStatus.BAD_REQUEST);
        }
    }

    private boolean userFindInDB(String email){
        return personRepository.findByEmail(email) != null;
    }

    private Integer addUserInDB(RegisterRequest userInfo, String token){
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

    private String getConfirmMessage(String token){
        return  "Hello, to complete the registration follow to link " +
                "<a href=\"" + address +
                "/registration/complete?token=" +
                token + "\">Confirm registration</a>";
    }
}
