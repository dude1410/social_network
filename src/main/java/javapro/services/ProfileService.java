package javapro.services;

import javapro.api.request.EditMyProfileRequest;
import javapro.api.response.LoginResponse;
import javapro.api.response.Response;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.DeletedPerson;
import javapro.model.Person;
import javapro.model.dto.DeletedPersonData;
import javapro.model.dto.MessageDTO;
import javapro.model.dto.auth.AuthorizedPerson;
import javapro.model.enums.DeletedType;
import javapro.repository.*;
import javapro.util.PersonToDtoMapper;
import javapro.util.Time;
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
    private final DeletedPersonRepository deletedPersonRepository;
    private final NotificationRepository notificationRepository;

    public ProfileService(PersonRepository personRepository,
                          PersonToDtoMapper personToDtoMapper,
                          CountryRepository countryRepository,
                          TownRepository townRepository,
                          DeletedPersonRepository deletedPersonRepository,
                          NotificationRepository notificationRepository) {
        this.personRepository = personRepository;
        this.personToDtoMapper = personToDtoMapper;
        this.countryRepository = countryRepository;
        this.townRepository = townRepository;
        this.deletedPersonRepository = deletedPersonRepository;
        this.notificationRepository = notificationRepository;
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
                .ok(new LoginResponse("successfully", Time.getTime(), authorizedPerson));
    }

    public ResponseEntity<Response<AuthorizedPerson>> getProfileById(Integer id) throws NotFoundException, BadRequestException {
        Optional<Person> personOpt = personRepository.findById(id);
        AuthorizedPerson authorizedPerson;

        if (personOpt.isPresent()) {
            Person person = personOpt.get();

            if (person.isBlocked()) {
                throw new BadRequestException(Config.STRING_PERSON_ISBLOCKED);
            }

            authorizedPerson = personToDtoMapper.convertToDto(person);
            var response = new Response<AuthorizedPerson>();
            response.setError("null");
            response.setTimestamp(Time.getTime());
            response.setData(authorizedPerson);
            authorizedPerson.setToken(null);
            return ResponseEntity.ok(response);
        } else {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }
    }


    public ResponseEntity<Response<AuthorizedPerson>> editMyProfile(EditMyProfileRequest editMyProfileRequest) throws AuthenticationException, NotFoundException, BadRequestException {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }
        var person = personRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());

        if (person.isBlocked()) {
            throw new BadRequestException(Config.STRING_PERSON_ISBLOCKED);
        }

        //about
        person.setAbout(editMyProfileRequest.getAbout() != null ? editMyProfileRequest.getAbout() : null);
        //birth day
        if (editMyProfileRequest.getBirthDate() != null) {
            if (editMyProfileRequest.getBirthDate().after(new Timestamp(System.currentTimeMillis()))) {
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
        if (!editMyProfileRequest.getFirstName().isEmpty()) {
            person.setFirstName(editMyProfileRequest.getFirstName());
        } else throw new BadRequestException(Config.STRING_PERSON_EMPTY_FISTNAME);


        //Last Name
        if (!editMyProfileRequest.getLastName().isEmpty()) {
            person.setLastName(editMyProfileRequest.getLastName());
        } else throw new BadRequestException(Config.STRING_PERSON_EMPTY_LASTNAME);

        personRepository.save(person);

        var response = new Response<AuthorizedPerson>();
        response.setError("successfully");
        response.setTimestamp(Time.getTime());
        var authorizedPerson = personToDtoMapper.convertToDto(person);
        authorizedPerson.setToken(null);
        response.setData(authorizedPerson);

        return ResponseEntity.ok(response);
    }

    public ResponseEntity<Response<MessageDTO>> deletePerson() throws BadRequestException, NotFoundException {

        var person = personRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());

        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }

        if (person.isBlocked()) {
            throw new BadRequestException(Config.STRING_PERSON_ISBLOCKED);
        }
        var deletedPersonData = new DeletedPersonData();

        person.setPhoto(deletedPersonData.getPhoto());
        person.setAbout(deletedPersonData.getAbout());
        person.setBirthDate(deletedPersonData.getBirthDate());
        person.setCountryId(deletedPersonData.getCountry());
        person.setTownId(deletedPersonData.getTown());
        person.setFirstName(deletedPersonData.getFirstName());
        person.setLastName(deletedPersonData.getLastName());
        person.setPhone(deletedPersonData.getPhone());
//      change person in db
        personRepository.save(person);

        DeletedPerson deletedPerson = new DeletedPerson();
        deletedPerson.setType(DeletedType.Temporarily.toString());
        deletedPerson.setPersonId(person.getId());

//      adding an entry in the deletion database "deleted_person"
        deletedPersonRepository.save(deletedPerson);


        notificationRepository.deleteAllByAuthorId(person.getId());

        var response = new Response<MessageDTO>();
        response.setError("Пользователь удален");
        response.setTimestamp(Time.getTime());
        response.setData(new MessageDTO());
        return ResponseEntity.ok(response);
    }

}

