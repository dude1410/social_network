package JavaPRO.services;

import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.RegisterErrorResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.MailConfig;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class PassRecoveryService {

    @Autowired
    EmailService emailService;

    @Autowired
    PersonRepository personRepository;

    @Autowired
    PasswordEncoder passwordEncoder;

    public ResponseEntity<?> passRecovery(String email){
        Person person = personRepository.findByEmail(email);
        if (person != null) {
            emailService.sendRecoveryMail(person.getConfirmationCode(), email);
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new RegisterErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
        }
    }

    public ResponseEntity<?> setNewPassword(SetPasswordRequest setPasswordRequest){
        String token = setPasswordRequest.getToken();
        String password = setPasswordRequest.getPassword();
        Person person = personRepository.findByConfirmationCode(token);
        if (person == null) {
            return new ResponseEntity<>(new RegisterErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
        }
        else {
            if (personRepository.setNewPassword(passwordEncoder.encode(password), token) != null) {
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new RegisterErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
            }
        }
    }

    private Long getTimestamp(){
        return (new Date().getTime() / 1000);
    }
}
