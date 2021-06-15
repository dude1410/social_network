package JavaPRO.services;

import JavaPRO.Util.PostToDTOMapper;
import JavaPRO.api.request.PostUpdateRequest;
import JavaPRO.api.response.*;
import JavaPRO.model.DTO.PostDTO;
import JavaPRO.model.DTO.PostDeleteDTO;
import JavaPRO.model.Post;
import JavaPRO.repository.PersonRepository;
import JavaPRO.repository.PostRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

@Slf4j
@Service
public class PostService {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final PostToDTOMapper postToDTOMapper;


    public PostService(PersonRepository personRepository, PostRepository postRepository, PostToDTOMapper postToDTOMapper) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.postToDTOMapper = postToDTOMapper;
    }

    public ResponseEntity<Response> searchPostsByText(String searchText) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("invalid_request", "UNAUTHORISED"));
        }

        if (searchText == null || searchText.length() == 0) {
            log.info(String.format("Input text is incorrect"));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("invalid_request", "BAD_REQUEST"));
        }
        searchText = searchText.toLowerCase(Locale.ROOT);

        List<Post> postList = postRepository.findPostsByText(searchText);

        List<PostDTO> postDTOList = new ArrayList();

        if (postList.size() == 0) {
            postList = postRepository.findAllPosts();
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

    public ResponseEntity<Response> getPostsByUser(int userID) {
        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("invalid_request", "UNAUTHORISED"));
        }

        List<Post> postList = postRepository.findPostsByAuthorID(userID);

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

    public ResponseEntity<Response> getAllPosts() {

        List<Post> postList = postRepository.findAllPosts();

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

    public ResponseEntity<Response> deletePostByID(int postID) {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            return ResponseEntity
                    .status(HttpStatus.UNAUTHORIZED)
                    .body(new ErrorResponse("invalid_request", "UNAUTHORISED"));
        }

        Post post = postRepository.findPostByID(postID);

        if (post == null) {
            log.info(String.format("ID doesn't exist"));
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(new ErrorResponse("invalid_request", "BAD_REQUEST"));
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

    public ResponseEntity<Response> updatePostByID(int id, String newTitle, String newPostText) {

        Post post = postRepository.findPostByID(id);

        post.setPostText(newPostText);
        post.setTitle(newTitle);

        postRepository.save(post);
        PostDTO postDTO = postToDTOMapper.convertToDTO(post);

        return ResponseEntity
                .ok(new PostUpdateResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<Response> getPostByID(int id) {

        Post post = postRepository.findPostByID(id);

        PostDTO postDTO = postToDTOMapper.convertToDTO(post);

        return ResponseEntity
                .ok(new PostUpdateResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<Response> publishPost(int userID, Long publishDate, PostUpdateRequest postUpdateRequest) {

        Post post = new Post();

        if (publishDate == null) {
            post.setTime(new Timestamp(System.currentTimeMillis()));
        } else {
            post.setTime(new Timestamp(publishDate));
        }

        post.setPostText(postUpdateRequest.getPost_text());
        post.setTitle(postUpdateRequest.getTitle());
        post.setBlocked(false);
        post.setAuthor(personRepository.findById(userID).get());

        Post postSaved = postRepository.save(post);

        PostDTO postDTO = postToDTOMapper.convertToDTO(postSaved);

        return ResponseEntity
                .ok(new PostUpdateResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

}