package javapro.services;

import javapro.util.PersonToDtoMapper;
import javapro.api.response.PersonsResponse;
import javapro.api.response.PostResponse;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.config.exception.UnAuthorizedException;
import javapro.model.dto.auth.AuthorizedPerson;
import javapro.model.dto.PostDTO;
import javapro.model.Person;
import javapro.model.PersonView;
import javapro.model.Post;
import javapro.repository.PersonRepository;
import javapro.repository.PersonViewRepository;
import javapro.repository.PostRepository;
import javapro.util.PostToDtoCustomMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.*;

@Slf4j
@Service
public class SearchService {

    private final PostRepository postRepository;
    private final PersonRepository personRepository;
    private final PersonViewRepository personViewRepository;

    private final PersonToDtoMapper personToDtoMapper;
    private final PostToDtoCustomMapper postToDTOCustomMapper;

    public SearchService(PostRepository postRepository,
                         PersonRepository personRepository,
                         PersonViewRepository personViewRepository,
                         PersonToDtoMapper personToDtoMapper) {
        this.postRepository = postRepository;
        this.personRepository = personRepository;
        this.personViewRepository = personViewRepository;
        this.personToDtoMapper = personToDtoMapper;

        this.postToDTOCustomMapper = Mappers.getMapper(PostToDtoCustomMapper.class);
    }

    public ResponseEntity<PersonsResponse> searchPeopleGeneral(String searchText,
                                                               Integer offset,
                                                               Integer itemPerPage) {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<PersonView> personFound = personViewRepository.searchPersonBy(searchText, pageable);

        List<Person> personList = new ArrayList<>();

        personFound.forEach(personView -> {
            try {
                personList.add(viewToPerson(personView));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });

        List<AuthorizedPerson> personDTOS = new ArrayList<>();

        personList.forEach(person -> personDTOS.add(personToDtoMapper.convertToDto(person)));

        return ResponseEntity
                .ok(new PersonsResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        (int) personFound.getTotalElements(),
                        offset,
                        itemPerPage,
                        personDTOS));
    }

    public ResponseEntity<PostResponse> searchPostsGeneral(String searchText,
                                                           Integer offset,
                                                           Integer itemPerPage) {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<Post> postsFound = postRepository.searchPostBy(searchText, pageable);

        List<PostDTO> postDTOS = new ArrayList<>();

        postsFound.forEach(post -> postDTOS.add(postToDTOCustomMapper.mapper(post)));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        (int) postsFound.getTotalElements(),
                        offset,
                        itemPerPage,
                        postDTOS));
    }

    public ResponseEntity<PersonsResponse> searchPeopleByProperties(
            String firstName,
            String lastName,
            Integer ageFrom,
            Integer ageTo,
            String country,
            String town,
            Integer offset,
            Integer itemPerPage
    ) throws UnAuthorizedException {

        checkAuthentication();

        //Преобразуем пришедшее нечто к рабочему формату
        firstName = stringFix(firstName);
        lastName = stringFix(lastName);
        ageFrom = ageFromFix(ageFrom);
        ageTo = ageToFix(ageTo);
        country = stringFix(country);
        town = stringFix(town);

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);
        Page<PersonView> personFound =
                personViewRepository.findPersonsByProperties(firstName, lastName, ageFrom, ageTo, country, town, pageable);

        List<Person> personList = new ArrayList<>();

        personFound.forEach(personView -> {
            try {
                personList.add(viewToPerson(personView));
            } catch (NotFoundException e) {
                e.printStackTrace();
            }
        });

        List<AuthorizedPerson> personDTOS = new ArrayList<>();

        personList.forEach(person -> personDTOS.add(personToDtoMapper.convertToDto(person)));

        return ResponseEntity
                .ok(new PersonsResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        (int) personFound.getTotalElements(),
                        offset,
                        itemPerPage,
                        personDTOS));
    }

    public ResponseEntity<PostResponse> searchPostsByProperties(String searchText,
                                                                Long dateFromLong,
                                                                Long dateToLong,
                                                                String searchAuthor,
                                                                String searchTag,
                                                                Integer offset,
                                                                Integer itemPerPage) throws BadRequestException, UnAuthorizedException {

        checkAuthentication();

        if (searchText.isBlank() || searchText.isEmpty()) {
            throw new BadRequestException(Config.STRING_NO_SEARCH_TEXT);
        }

        searchText = stringFix(searchText);
        searchAuthor = stringFix(searchAuthor);
        searchTag = stringFix(searchTag);
        Date dateFrom = dateFromFix(dateFromLong);
        Date dateTo = dateToFix(dateToLong);

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);

        Page<Post> postList = postRepository.findPostsByProperties(searchText, dateFrom, dateTo, searchAuthor, searchTag, pageable);

        List<PostDTO> postDTOList = new ArrayList();

        postList.forEach(post -> postDTOList.add(postToDTOCustomMapper.mapper(post)));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        (int) postList.getTotalElements(),
                        offset,
                        itemPerPage,
                        postDTOList));
    }

    private void checkAuthentication() throws UnAuthorizedException {
        if (SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName() == null) {
            throw new UnAuthorizedException(Config.STRING_AUTH_ERROR);
        }
    }

    /**
     * Внутренние методы доя обработки данных
     */

    //Конвертер строки из View в строку из таблицы
    private Person viewToPerson(PersonView personView) throws NotFoundException {

        Optional<Person> person = personRepository.findById(personView.getId());

        if (person.isEmpty()) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        } else {
            return person.get();
        }
    }

    private String stringFix(String variable) {
        if (variable == null || variable.length() == 0) {
            return "";
        } else {
            return variable.toLowerCase(Locale.ROOT);
        }
    }

    private Integer ageFromFix(Integer ageFrom) {
        if (ageFrom == null) {
            return 0;
        }
        return ageFrom;
    }

    private Integer ageToFix(Integer ageTo) {
        if (ageTo == null) {
            return 200;
        }
        return ageTo;
    }

    private Date dateFromFix(Long dateFromLong) {

        if (dateFromLong != null) {
            return new Date(dateFromLong);
        } else {
            //нижняя граница 18.06.1970, 07:36:56
            return new Timestamp(14531816);
        }
    }

    private Date dateToFix(Long dateToLong) {

        if (dateToLong != null) {
            return new Timestamp(dateToLong);
        } else {
            //верхняя граница - сейчас, т.к. посты из будущего мы не видим
            return new Date();
        }
    }
}
