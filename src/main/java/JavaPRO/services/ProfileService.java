package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.api.response.ErrorResponse;
import JavaPRO.api.response.LoginResponse;
import JavaPRO.api.response.Response;
import JavaPRO.config.Config;
import JavaPRO.config.exception.AuthenticationException;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.repository.PersonRepository;
import JavaPRO.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Slf4j
@Service
public class ProfileService {

    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final PersonToDtoMapper personToDtoMapper;

    public ProfileService(PersonRepository personRepository,
                          PostRepository postRepository,
                          PersonToDtoMapper personToDtoMapper) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.personToDtoMapper = personToDtoMapper;
    }

    public ResponseEntity<LoginResponse> getMyProfile() throws AuthenticationException,
            NotFoundException {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }

        String personEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();
        var userFromDB = personRepository.findByEmailForLogin(personEmail);

        if (userFromDB == null) {
            log.info(String.format("User with email '%s' is not found!", userFromDB)); // todo: что передается?
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
        log.info(String.format("User with email '%s' found: %s", userFromDB, userFromDB));

        var authorizedPerson = personToDtoMapper.convertToDto(userFromDB);

        authorizedPerson.setToken(null);
        return ResponseEntity
                .ok(new LoginResponse("successfully", new Timestamp(System.currentTimeMillis()).getTime(), authorizedPerson));
    }
}

