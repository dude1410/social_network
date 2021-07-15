package javapro.services;

import javapro.util.PersonToDtoMapper;
import javapro.api.request.EditMyProfileRequest;
import javapro.api.response.*;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.Person;
import javapro.repository.CountryRepository;
import javapro.repository.PersonRepository;
import javapro.repository.TownRepository;
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
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
        var authorizedPerson = personToDtoMapper.convertToDto(userFromDB);

        authorizedPerson.setToken(null);
        return ResponseEntity
                .ok(new LoginResponse("successfully", new Timestamp(System.currentTimeMillis()).getTime(), authorizedPerson));
    }

    public ResponseEntity<ProfileByIdResponse> getProfileById(Integer id) throws BadRequestException {
        Optional<Person> personOpt = personRepository.findById(id);
        if (personOpt.isPresent()) {
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
        } else {
            throw new BadRequestException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
    }


    public ResponseEntity<Response> editMyProfile(EditMyProfileRequest editMyProfileRequest) throws AuthenticationException, NotFoundException, BadRequestException {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }
        var person = personRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());

        //about
        if (editMyProfileRequest.getAbout() != null) {
            person.setAbout(editMyProfileRequest.getAbout());
        }
        //birth day
        if (editMyProfileRequest.getBirthDate() != null) {
            if(editMyProfileRequest.getBirthDate().after(new Timestamp(System.currentTimeMillis()))){
                throw new BadRequestException(Config.STRING_WRONG_DATA);
            }
            person.setBirthDate(editMyProfileRequest.getBirthDate());
        }
        //country
        if (editMyProfileRequest.getCountryId() != null) {
            person.setCountryId(countryRepository.findById(editMyProfileRequest.getCountryId())
                    .orElseThrow(() -> new NotFoundException(Config.STRING_BAD_REQUEST)));
        }
        //town
        if (editMyProfileRequest.getTownId() != null) {
            person.setTownId(townRepository.findById(editMyProfileRequest.getTownId())
                    .orElseThrow(() -> new NotFoundException(Config.STRING_BAD_REQUEST)));
        }
        //phone
        if (editMyProfileRequest.getPhone() != null) {
            person.setPhone(editMyProfileRequest.getPhone());
        }
        //First Name
        if (editMyProfileRequest.getFirstName() != null) {
            person.setFirstName(editMyProfileRequest.getFirstName());
        }
        //Last Name
        if (editMyProfileRequest.getLastName() != null) {
            person.setLastName(editMyProfileRequest.getLastName());
        }

        personRepository.save(person);

        var response = new Response();
        response.setError("successfully");
        response.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        var authorizedPerson = personToDtoMapper.convertToDto(person);
        authorizedPerson.setToken(null);
        response.setData(authorizedPerson);

        return ResponseEntity.ok(response);
    }

}

