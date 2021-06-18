package JavaPRO.services;

import JavaPRO.api.request.SetPasswordRequest;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.ResponseData;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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

    @Value("${spring.mail.address}")
    private String address;

    public ResponseEntity<OkResponse> passRecovery(String email) throws BadRequestException, NotFoundException {

        if (email == null) {
            throw new BadRequestException(Config.STRING_AUTH_INVALID_EMAIL);
        }
        Person person = personRepository.findByEmail(email);
        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
        String messageBody = "Hello, to recovery your password follow to link " +
                "<a href=\"" + address + "/change-password?token=" +
                person.getConfirmationCode() + "\">Password recovery</a>";
        emailService.sendMail("Recovery password in social network", messageBody, email);
        return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
    }

    public ResponseEntity<OkResponse> setNewPassword(SetPasswordRequest setPasswordRequest) throws BadRequestException, NotFoundException {
        if (setPasswordRequest.getPassword() == null || setPasswordRequest.getToken() == null) {
            throw new BadRequestException(Config.STRING_BAD_REQUEST);
        }
        String token = setPasswordRequest.getToken();
        String password = setPasswordRequest.getPassword();
        Person person = personRepository.findByConfirmationCode(token);
        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        } else {
            if (personRepository.setNewPassword(passwordEncoder.encode(password), token) != null) {
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            } else {
                throw new BadRequestException(Config.STRING_INVALID_SET_PASSWORD);
            }
        }
    }

    private Long getTimestamp() {
        return (new Date().getTime() / 1000);
    }
}
