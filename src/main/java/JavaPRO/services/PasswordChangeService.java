package JavaPRO.services;

import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.Response;
import JavaPRO.api.response.ResponseData;
import JavaPRO.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Service
public class PasswordChangeService {

    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public PasswordChangeService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public ResponseEntity<Response> changePassword(String newPassword, String currentEmail){
        if (personRepository.setNewPasswordByEmail(passwordEncoder.encode(newPassword), currentEmail) != 0){
            return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
        }
        else {
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
        }
    }

    private Long getTimestamp(){
        return (new Date().getTime() / 1000);
    }
}
