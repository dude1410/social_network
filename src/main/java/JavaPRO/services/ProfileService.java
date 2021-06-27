package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.api.response.*;
import JavaPRO.config.Config;
import JavaPRO.config.exception.AuthenticationException;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.model.Person;
import JavaPRO.repository.PersonRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import java.sql.Timestamp;
import java.util.Optional;

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

    public ResponseEntity<ProfileByIdResponse> getProfileById(Integer id) throws BadRequestException {
        Optional<Person> personOpt = personRepository.findById(id);
        if (personOpt.isPresent()){
            Person person = personOpt.get();
            ProfileByIdData profileByIdData = new ProfileByIdData(
                    person.getId(),
                    person.getFirstName(),
                    person.getLastName(),
                    person.getRegDate() == null ? null : person.getRegDate().getTime(),
                    person.getBirthDate() == null ? null : person.getRegDate().getTime(),
                    person.getEmail(),
                    person.getPhone(),
                    person.getPhoto(),
                    person.getAbout(),
                    new CityData(person.getTownId() == null ? null : person.getTownId().getId(),
                                 person.getTownId() == null ? null : person.getTownId().getName()),
                    new CountryData(person.getCountryId() == null ? null : person.getCountryId().getId(),
                                    person.getCountryId() == null ? null : person.getCountryId().getName()),
                    person.getMessagesPermission().toString(),
                    person.getLastOnlineTime().getTime(),
                    person.isBlocked()
            );
            return ResponseEntity.ok(new ProfileByIdResponse("null", new Timestamp(System.currentTimeMillis()).getTime(), profileByIdData));
        }
        else {
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
    }
}

