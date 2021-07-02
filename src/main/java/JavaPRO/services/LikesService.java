package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.api.response.IsLikedResponse;
import JavaPRO.api.response.LikeResponse;
import JavaPRO.config.Config;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.model.DTO.Auth.AuthorizedPerson;
import JavaPRO.model.DTO.IsLikedDTO;
import JavaPRO.model.DTO.LikeDTO;
import JavaPRO.model.Person;
import JavaPRO.model.Post;
import JavaPRO.model.PostLike;
import JavaPRO.repository.LikeRepository;
import JavaPRO.repository.PersonRepository;
import JavaPRO.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Slf4j
@Service
public class LikesService {
    private final LikeRepository likeRepository;
    private final PostRepository postRepository;
    private final PersonToDtoMapper personToDtoMapper;
    private final PersonRepository personRepository;

    public LikesService(LikeRepository likeRepository,
                        PostRepository postRepository,
                        PersonToDtoMapper personToDtoMapper,
                        PersonRepository personRepository) {
        this.likeRepository = likeRepository;
        this.postRepository = postRepository;
        this.personToDtoMapper = personToDtoMapper;
        this.personRepository = personRepository;
    }

    public ResponseEntity<IsLikedResponse> isLiked(Integer postID) throws NotFoundException, BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Integer personID = getCurrentUser().getId();
        boolean isLiked = false;

        if (likeRepository.isUserLikedPost(personID, postID) > 0) {
            isLiked = true;
        }

        IsLikedDTO isLikedDTO = new IsLikedDTO();

        isLikedDTO.setLikes(isLiked);

        return ResponseEntity
                .ok(new IsLikedResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        isLikedDTO
                ));
    }

    public ResponseEntity<LikeResponse> addLike(Integer postID) throws NotFoundException, BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Person currentUser = getCurrentUser();
        //если самолайк
        if (postRepository.getAuthorIDByPostID(postID).equals(currentUser.getId())) {
            return ResponseEntity
                    .ok(new LikeResponse("successfully",
                            new Timestamp(System.currentTimeMillis()).getTime(),
                            null
                    ));
        }
        //если лайк уже есть
        if (likeRepository.isUserLikedPost(currentUser.getId(), postID) > 0) {
            return ResponseEntity
                    .ok(new LikeResponse("successfully",
                            new Timestamp(System.currentTimeMillis()).getTime(),
                            null
                    ));
        }

        Post likedPost = postRepository.findPostByID(postID);

        if (likedPost == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }
        Integer likesCount = likeRepository.getLikes(postID);
        List<Person> persons = likeRepository.getUsersWhoLikedPost(postID);

        PostLike like = new PostLike();
        like.setTime(new Date());
        like.setPerson(currentUser);
        like.setPost(likedPost);

        likeRepository.save(like);

        List<AuthorizedPerson> personDTOS = new ArrayList<>();

        if (!persons.isEmpty()) {
            persons.forEach(person -> personDTOS.add(personToDtoMapper.convertToDto(person)));
        }

        LikeDTO likesDTO = new LikeDTO();

        likesDTO.setLikes(++likesCount);
        likesDTO.setUsers(personDTOS);

        return ResponseEntity
                .ok(new LikeResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    public ResponseEntity<LikeResponse> deleteLike(Integer postID) throws NotFoundException, BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Integer personID = getCurrentUser().getId();

        likeRepository.deleteLikeOnPost(personID, postID);
        Integer likesCount = likeRepository.getLikes(postID);

        LikeDTO likesDTO = new LikeDTO();
        likesDTO.setLikes(likesCount);

        return ResponseEntity
                .ok(new LikeResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    public ResponseEntity<LikeResponse> getAllLikes(Integer postID) throws BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Integer likesCount = likeRepository.getLikes(postID);
        List<Person> persons = likeRepository.getUsersWhoLikedPost(postID);

        LikeDTO likesDTO = new LikeDTO();

        likesDTO.setLikes(likesCount);

        if (likesCount > 0) {
            List<AuthorizedPerson> personDTOS = null;
            persons.forEach(person -> personDTOS.add(personToDtoMapper.convertToDto(person)));
            likesDTO.setUsers(personDTOS);
        }

        return ResponseEntity
                .ok(new LikeResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    private Person getCurrentUser() throws NotFoundException {
        String personEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        Person person = personRepository.findByEmail(personEmail);

        if (person == null) {
            throw new NotFoundException(Config.STRING_NO_PERSON_IN_DB);
        }

        return person;
    }

}
