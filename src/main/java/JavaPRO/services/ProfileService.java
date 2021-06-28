package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.api.request.EditMyProfileRequest;
import JavaPRO.api.response.*;
import JavaPRO.config.Config;
import JavaPRO.config.exception.AuthenticationException;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.model.Person;
import JavaPRO.repository.CountryRepository;
import JavaPRO.repository.PersonRepository;
import JavaPRO.repository.TownRepository;
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
    private final CountryRepository countryRepository;
    private final TownRepository townRepository;


    public ProfileService(PersonRepository personRepository,
                          PersonToDtoMapper personToDtoMapper,
                          CountryRepository countryRepository,
                          TownRepository townRepository) {
        this.personRepository = personRepository;
        this.personToDtoMapper = personToDtoMapper;
        this.countryRepository = countryRepository;
        this.townRepository = townRepository;
    }


    public ResponseEntity<Response> getMyProfile() throws AuthenticationException,
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
        var response = new Response();
        response.setError("successfully");
        response.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        response.setData(authorizedPerson);

        return ResponseEntity.ok(response);
    }


    public ResponseEntity<Response> editMyProfile(EditMyProfileRequest editMyProfileRequest) throws AuthenticationException {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }
        var person = personRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());


        if(editMyProfileRequest.getAbout() != null){
            person.setAbout(editMyProfileRequest.getAbout());
        }
//        if(editMyProfileRequest.getBirthDate() != null){
//            person.setBirthDate(Timestamp.valueOf(editMyProfileRequest.getBirthDate()));
//        }
        if(editMyProfileRequest.getCountryId() != null){
            person.setCountryId(countryRepository.findByName(editMyProfileRequest.getCountryId()).get());
        }

        if(editMyProfileRequest.getTownId() != null){
            person.setTownId(townRepository.findByName(editMyProfileRequest.getTownId()).get());
        }
        if(editMyProfileRequest.getPhone() != null){
            person.setPhone(editMyProfileRequest.getPhone());
        }
        if (editMyProfileRequest.getFirstName() != null) {
            person.setFirstName(editMyProfileRequest.getFirstName());
        }
        if (editMyProfileRequest.getLastName() != null) {
            person.setLastName(editMyProfileRequest.getLastName());
        }
        personRepository.save(person);

        Response response = new Response();
        response.setError("successfully");
        response.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        var authorizedPerson = personToDtoMapper.convertToDto(person);
        authorizedPerson.setToken(null);
        response.setData(authorizedPerson);

        return ResponseEntity.ok(response);
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

