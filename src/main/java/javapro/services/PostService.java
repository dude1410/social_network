package javapro.services;

import javapro.util.PersonToDtoMapper;
import javapro.util.PostToDTOMapper;
import javapro.api.request.PostDataRequest;
import javapro.api.response.*;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.*;
import javapro.model.dto.*;
import javapro.repository.LikeRepository;
import javapro.repository.PersonRepository;
import javapro.repository.PostRepository;
import javapro.util.PostToDTOMapper;
import lombok.extern.slf4j.Slf4j;
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
public class PostService {
    private final PersonRepository personRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;

    private final PostToDTOMapper postToDTOMapper;

    public PostService(PersonRepository personRepository,
                       PostRepository postRepository,
                       LikeRepository likeRepository,
                       PostToDTOMapper postToDTOMapper) throws NotFoundException {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.postToDTOMapper = postToDTOMapper;
    }

    public ResponseEntity<MyWallResponse> getPostsByUser(Integer offset, Integer itemPerPage) throws NotFoundException {

        Person person = getCurrentUser();

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);

        Page<Post> postList = postRepository.findPostsByAuthorID(person.getId(), pageable);

        if (postList.isEmpty()) {
            throw new NotFoundException(Config.STRING_NO_POSTS_IN_DB);
        }

        List<PostDTO> postDTOList = new ArrayList<>();

        postList.forEach(post -> postDTOList.add(postToDTOMapper.convertToDTO(post)));

        postDTOList.forEach(PostDTO::setPostStatus);
        postDTOList.forEach(postDTO -> postDTO.setLikes(likeRepository.getLikes(postDTO.getId())));

        return ResponseEntity
                .ok(new MyWallResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        (int) postList.getTotalElements(),
                        offset,
                        itemPerPage,
                        postDTOList
                ));
    }

    public ResponseEntity<PostResponse> getAllPosts(Integer offset, Integer itemPerPage) throws NotFoundException {

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);

        Page<Post> postList = postRepository.findAllPosts(new Date(), pageable);

        if (postList.isEmpty()) {
            throw new NotFoundException(Config.STRING_NO_POSTS_IN_DB);
        }

        List<PostDTO> postDTOList = new ArrayList();

        postList.forEach(post -> postDTOList.add(postToDTOMapper.convertToDTO(post)));
        postDTOList.forEach(postDTO -> postDTO.setLikes(likeRepository.getLikes(postDTO.getId())));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        (int) postList.getTotalElements(),
                        offset,
                        itemPerPage,
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

        post.setDeleted(true);
        postRepository.save(post);

        PostDeleteDTO postDeleteDTO = new PostDeleteDTO();
        postDeleteDTO.setId(postID);

        return ResponseEntity
                .ok(new DeletePostByIDResponse(
                        "successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDeleteDTO
                ));
    }

    public ResponseEntity<PostShortResponse> updatePostByID(Integer postID,
                                                            PostDataRequest postDataRequest) throws NotFoundException,
            BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Post post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        post.setPostText(postDataRequest.getPost_text());
        post.setTitle(postDataRequest.getTitle());

        postRepository.save(post);

        PostDTO postDTO = postToDTOMapper.convertToDTO(post);
        postDTO.setLikes(likeRepository.getLikes(postDTO.getId()));

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<PostShortResponse> getPostByID(Integer postID) throws NotFoundException, BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Post post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        PostDTO postDTO = postToDTOMapper.convertToDTO(post);
        postDTO.setLikes(likeRepository.getLikes(postDTO.getId()));
        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<PostShortResponse> publishPost(Long publishDate,
                                                         PostDataRequest postDataRequest) throws NotFoundException {
        Person currentUser = getCurrentUser();

        Post post = new Post();

        if (publishDate == null) {
            post.setTime(new Timestamp(System.currentTimeMillis()));
        } else {
            post.setTime(new Timestamp(publishDate));
        }
        post.setPostText(postDataRequest.getPost_text());
        post.setTitle(postDataRequest.getTitle());
        post.setBlocked(false);
        post.setAuthor(currentUser);

        postRepository.save(post);

        PostDTO postDTO = postToDTOMapper.convertToDTO(post);

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<PostShortResponse> recoverPost(Integer postID) throws BadRequestException, NotFoundException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Post post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        post.setDeleted(false);

        postRepository.save(post);

        PostDTO postDTO = postToDTOMapper.convertToDTO(post);
        postDTO.setLikes(likeRepository.getLikes(postDTO.getId()));
        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<ReportCommentResponse> reportPost(Integer postID) throws BadRequestException, NotFoundException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Post post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        //TODO отправляем id поста куда-то

        return ResponseEntity
                .ok(new ReportCommentResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        new MessageDTO()
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