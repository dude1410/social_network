package JavaPRO.services;

import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.Response;
import JavaPRO.api.response.ResponseData;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PassRecoveryService {

    private final EmailService emailService;
    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${spring.mail.address}")
    private String address;

    public PassRecoveryService(EmailService emailService, PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.emailService = emailService;
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Response> passRecovery(String email){
        Person person = personRepository.findByEmail(email);
        if (person != null) {
            String messageBody = "Hello, to recovery your password follow to link " +
                                "<a href=\"" + address + "/change-password?token=" +
                                person.getConfirmationCode() + "\">Password recovery</a>";
            emailService.sendMail("Recovery password in social network", messageBody, email);
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<Response> setNewPassword(SetPasswordRequest setPasswordRequest){
        String token = setPasswordRequest.getToken();
        String password = setPasswordRequest.getPassword();
        Person person = personRepository.findByConfirmationCode(token);
        if (person == null) {
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
        }
        else {
            if (personRepository.setNewPassword(passwordEncoder.encode(password), token) != null) {
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
            }
        }
    }

    private Long getTimestamp(){
        return (new Date().getTime() / 1000);
    }
}
