package JavaPRO.services;

import JavaPRO.Util.PostToDTOMapper;
import JavaPRO.api.request.PostUpdateRequest;
import JavaPRO.api.response.*;
import JavaPRO.config.Config;
import JavaPRO.config.exception.AuthenticationException;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.model.DTO.PostDTO;
import JavaPRO.model.DTO.PostDeleteDTO;
import JavaPRO.model.Person;
import JavaPRO.model.Post;
import JavaPRO.repository.PersonRepository;
import JavaPRO.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class PostService {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final PostToDTOMapper postToDTOMapper;


    public PostService(PersonRepository personRepository,
                       PostRepository postRepository,
                       PostToDTOMapper postToDTOMapper) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.postToDTOMapper = postToDTOMapper;
    }

    public ResponseEntity<PostResponse> searchPostsByText(String searchText) throws BadRequestException,
            AuthenticationException {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }

        if (searchText == null || searchText.length() == 0) {
            log.info(String.format("Input text is incorrect"));
            throw new BadRequestException(Config.STRING_NO_SEARCH_TEXT);
        }
        searchText = searchText.toLowerCase(Locale.ROOT);

        List<Post> postList = postRepository.findPostsByText(searchText);

        List<PostDTO> postDTOList = new ArrayList();

        if (postList.size() == 0) {
            postList = postRepository.findAllPosts(new Date());
        }

        postList.forEach(post -> postDTOList.add(postToDTOMapper.convertToDTO(post)));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0l,
                        0l,
                        20l,
                        postDTOList));
    }

    public ResponseEntity<MyWallResponse> getPostsByUser(Integer userID) throws AuthenticationException,
            NotFoundException,
            BadRequestException {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }
        if (userID == null) {
            throw new BadRequestException(Config.STRING_NO_USER_ID);
        }

        List<Post> postList = postRepository.findPostsByAuthorID(userID);

        if (postList.isEmpty()) {
            throw new NotFoundException(Config.STRING_NO_POSTS_IN_DB);
        }

        List<PostDTO> postDTOList = new ArrayList<>();

        postList.forEach(post -> postDTOList.add(postToDTOMapper.convertToDTO(post)));

        postDTOList.forEach(PostDTO::setPostStatus);

        return ResponseEntity
                .ok(new MyWallResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0l,
                        0l,
                        20l,
                        postDTOList
                ));
    }

    public ResponseEntity<PostResponse> getAllPosts() throws NotFoundException {

        List<Post> postList = postRepository.findAllPosts(new Date());

        if (postList.isEmpty()) {
            throw new NotFoundException(Config.STRING_NO_POSTS_IN_DB);
        }

        List<PostDTO> postDTOList = new ArrayList();

        postList.forEach(post -> postDTOList.add(postToDTOMapper.convertToDTO(post)));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0l,
                        0l,
                        20l,
                        postDTOList
                ));
    }

    public ResponseEntity<DeletePostByIDResponse> deletePostByID(Integer postID) throws AuthenticationException,
            NotFoundException, BadRequestException {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Post post = postRepository.findPostByID(postID);

        if (post == null) {
            log.info(String.format("ID doesn't exist"));
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        int id = postRepository.deletePostByID(postID);

        PostDeleteDTO postDeleteDTO = new PostDeleteDTO();
        postDeleteDTO.setId(id);

        return ResponseEntity
                .ok(new DeletePostByIDResponse(
                        "successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDeleteDTO
                ));
    }

    public ResponseEntity<PostShortResponse> updatePostByID(Integer id,
                                                            String newTitle,
                                                            String newPostText) throws NotFoundException,
            BadRequestException {

        Post post = postRepository.findPostByID(id);

        if (id == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        post.setPostText(newPostText);
        post.setTitle(newTitle);

        postRepository.save(post);
        PostDTO postDTO = postToDTOMapper.convertToDTO(post);

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<PostShortResponse> getPostByID(Integer id) throws NotFoundException, BadRequestException {

        if (id == null) {
            throw new BadRequestException(Config.STRING_NO_POST_IN_DB);
        }
        Post post = postRepository.findPostByID(id);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        PostDTO postDTO = postToDTOMapper.convertToDTO(post);

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<PostShortResponse> publishPost(Integer userID,
                                                         Long publishDate,
                                                         PostUpdateRequest postUpdateRequest) throws BadRequestException, NotFoundException {

        if (userID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_IN_DB);
        }

        Post post = new Post();

        if (publishDate == null) {
            post.setTime(new Timestamp(System.currentTimeMillis()));
        } else {
            post.setTime(new Timestamp(publishDate));
        }

        post.setPostText(postUpdateRequest.getPost_text());
        post.setTitle(postUpdateRequest.getTitle());
        post.setBlocked(false);

        Person person = personRepository.findById(userID).get();

        if (person == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        post.setAuthor(person);

        Post postSaved = postRepository.save(post);

        PostDTO postDTO = postToDTOMapper.convertToDTO(postSaved);

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }
}