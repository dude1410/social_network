package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.LoginResponce;
import JavaPRO.api.response.Response;
import JavaPRO.config.Config;
import JavaPRO.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class ProfileService {

    private final PersonRepository personRepository;
    private final PersonToDtoMapper personToDtoMapper;

    public ProfileService(PersonRepository personRepository,
                          PersonToDtoMapper personToDtoMapper) {
        this.personRepository = personRepository;
        this.personToDtoMapper = personToDtoMapper;
    }


    public ResponseEntity<Response> getMyProfile() {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("invalid_request", Config.STRING_AUTH_UNAUTHORISED));
        }

        String personEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        var userFromDB = personRepository.findByEmailForLogin(personEmail);

        if (userFromDB == null) {
            log.info(String.format("User with email '%s' is not found!", userFromDB));
            return ResponseEntity
                    .badRequest()
                    .body(new ErrorResponse("e-mail not found", Config.STRING_AUTH_LOGIN_NO_SUCH_USER));
        }
        log.info(String.format("User with email '%s' found: %s", userFromDB, userFromDB));

        var authorizedPerson = personToDtoMapper.convertToDto(userFromDB);

        authorizedPerson.setToken(null);
        return ResponseEntity
                .ok(new LoginResponce("successfully", new Date().getTime(), authorizedPerson));
    }
}

