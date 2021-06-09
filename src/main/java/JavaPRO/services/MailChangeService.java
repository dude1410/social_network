package JavaPRO.services;

import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.Response;
import JavaPRO.api.response.ResponseData;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.util.Date;

@Service
public class MailChangeService {

    private final PersonRepository personRepository;

    public MailChangeService(PersonRepository personRepository) {
        this.personRepository = personRepository;
    }

    public ResponseEntity<Response> changeMail(String newEmail, String currentEmail){
        Person person = personRepository.findByEmail(currentEmail);
        if (person != null) {
            if (personRepository.setNewMail(newEmail, currentEmail) != null) {
                return new ResponseEntity<>(new OkResponse("null", getTimestamp(), new ResponseData("OK")), HttpStatus.OK);
            }
            else {
                return new ResponseEntity<>(new ErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
            }
        }
        else {
            return new ResponseEntity<>(new ErrorResponse("invalid_request", "password recovery error"), HttpStatus.BAD_REQUEST);
        }
    }

    private Long getTimestamp(){
        return (new Date().getTime() / 1000);
    }
}
