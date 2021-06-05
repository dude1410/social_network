package JavaPRO.services;

import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.OkResponse;
import JavaPRO.api.response.Response;
import JavaPRO.api.response.ResponseData;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import java.security.Principal;
import java.util.Date;

@Service
public class MailChangeService {

    @Autowired
    PersonRepository personRepository;

    public ResponseEntity<Response> changeMail(String email, String mail){
        Person person = personRepository.findByEmail(mail);
        if (person != null) {
            if (personRepository.setNewMail(email, person.getId()) != null) {
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
