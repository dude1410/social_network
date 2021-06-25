package JavaPRO.services;

import JavaPRO.Util.PersonToDtoMapper;
import JavaPRO.Util.PostToDTOMapper;
import JavaPRO.Util.TagToDTOMapper;
import JavaPRO.api.request.PostUpdateRequest;
import JavaPRO.api.request.TagRequest;
import JavaPRO.api.response.*;
import JavaPRO.config.Config;
import JavaPRO.config.exception.AuthenticationException;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.config.exception.NotFoundException;
import JavaPRO.model.*;
import JavaPRO.model.DTO.*;
import JavaPRO.model.DTO.Auth.AuthorizedPerson;
import JavaPRO.repository.LikeRepository;
import JavaPRO.repository.PersonRepository;
import JavaPRO.repository.PostRepository;
import JavaPRO.repository.TagRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

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
    private final LikeRepository likeRepository;
    private final TagRepository tagRepository;

    private final PostToDTOMapper postToDTOMapper;
    private final PersonToDtoMapper personToDtoMapper;
    private final TagToDTOMapper tagToDTOMapper;

    public PostService(PersonRepository personRepository,
                       PostRepository postRepository,
                       LikeRepository likeRepository, TagRepository tagRepository, PostToDTOMapper postToDTOMapper, PersonToDtoMapper personToDtoMapper, TagToDTOMapper tagToDTOMapper) throws NotFoundException {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.tagRepository = tagRepository;
        this.postToDTOMapper = postToDTOMapper;
        this.personToDtoMapper = personToDtoMapper;
        this.tagToDTOMapper = tagToDTOMapper;
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
        postDTOList.forEach(postDTO -> postDTO.setLikes(postRepository));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0,
                        0,
                        20,
                        postDTOList));
    }

    public ResponseEntity<MyWallResponse> getPostsByUser() throws NotFoundException {

        Person person = getCurrentUser();

        List<Post> postList = postRepository.findPostsByAuthorID(person.getId());

        if (postList.isEmpty()) {
            throw new NotFoundException(Config.STRING_NO_POSTS_IN_DB);
        }

        List<PostDTO> postDTOList = new ArrayList<>();

        postList.forEach(post -> postDTOList.add(postToDTOMapper.convertToDTO(post)));

        postDTOList.forEach(PostDTO::setPostStatus);
        postDTOList.forEach(postDTO -> postDTO.setLikes(postRepository));
        return ResponseEntity
                .ok(new MyWallResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0,
                        0,
                        20,
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

        postDTOList.forEach(postDTO -> postDTO.setLikes(postRepository));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0,
                        0,
                        20,
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

    public ResponseEntity<PostShortResponse> updatePostByID(Integer postID,
                                                            String newTitle,
                                                            String newPostText) throws NotFoundException,
            BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Post post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        post.setPostText(newPostText);
        post.setTitle(newTitle);

        postRepository.save(post);

        PostDTO postDTO = postToDTOMapper.convertToDTO(post);
        postDTO.setLikes(postRepository);
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
        postDTO.setLikes(postRepository);
        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<PostShortResponse> publishPost(Long publishDate,
                                                         PostUpdateRequest postUpdateRequest) throws NotFoundException {
        Person currentUser = getCurrentUser();

        Post post = new Post();

        if (publishDate == null) {
            post.setTime(new Timestamp(System.currentTimeMillis()));
        } else {
            post.setTime(new Timestamp(publishDate));
        }
        post.setPostText(postUpdateRequest.getPost_text());
        post.setTitle(postUpdateRequest.getTitle());
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

    public ResponseEntity<IsLikedResponse> isLiked(Integer postID) throws NotFoundException {

        Integer personID = getCurrentUser().getId();
        boolean isLiked = false;

        if (postRepository.isUserLikedPost(personID, postID) > 0) {
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

    public ResponseEntity<LikeResponse> addLike(Integer postID) throws NotFoundException {
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
        if (postRepository.isUserLikedPost(currentUser.getId(), postID) > 0) {
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
        Integer likesCount = postRepository.getLikes(postID);
        List<Person> persons = postRepository.getUsersWhoLikedPost(postID);

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

    public ResponseEntity<LikeResponse> deleteLike(Integer postID) throws NotFoundException {

        Integer personID = getCurrentUser().getId();

        postRepository.deleteLikeOnPost(personID, postID);
        Integer likesCount = postRepository.getLikes(postID);

        LikeDTO likesDTO = new LikeDTO();
        likesDTO.setLikes(likesCount);

        return ResponseEntity
                .ok(new LikeResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    public ResponseEntity<LikeResponse> getAllLikes(Integer objectID) {

        Integer likesCount = postRepository.getLikes(objectID);
        List<Person> persons = postRepository.getUsersWhoLikedPost(objectID);

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

    public ResponseEntity<TagResponse> addTag(TagRequest tagRequest){

        Tag newTag = new Tag();
        newTag.setTag(tagRequest.getTag());

        PostToTag postToTag = new PostToTag();
        //postToTag.setId(new PostTagPK());

        tagRepository.save(newTag);

        TagDTO tagDTO = tagToDTOMapper.convertToDto(newTag);

        return ResponseEntity
                .ok(new TagResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        tagDTO
                ));
    }

    public ResponseEntity<TagsResponse> getTags(String tagText) {

        List<Tag> tagsList = tagRepository.findTagByText(tagText);

        List<TagDTO> tagDTOs = new ArrayList<>();

        tagsList.forEach(tag -> tagDTOs.add(tagToDTOMapper.convertToDto(tag)));

        return ResponseEntity
                .ok(new TagsResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0,
                        0,
                        20,
                        tagDTOs
                ));
    }

    public ResponseEntity<TagDeleteResponse> deleteTag(Integer tagID){

        tagRepository.deleteById(tagID);
        
        TagDeleteDTO tagDeleteDTO = new TagDeleteDTO();

        return ResponseEntity
                .ok(new TagDeleteResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        tagDeleteDTO
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