package javapro.services;

import javapro.api.request.CommentBodyRequest;
import javapro.api.response.*;
import javapro.config.Config;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.*;
import javapro.model.dto.*;
import javapro.model.dto.auth.AuthorizedPerson;
import javapro.model.enums.NotificationType;
import javapro.repository.*;
import javapro.util.CommentToDTOMapper;
import javapro.util.PersonToDtoMapper;
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
public class PostCommentService {
    private final PersonRepository personRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationEntityRepository notificationEntityRepository;

    private final PersonToDtoMapper personToDtoMapper;
    private final CommentToDTOMapper commentToDTOMapper;

    public PostCommentService(PersonRepository personRepository,
                              CommentRepository commentRepository,
                              PostRepository postRepository,
                              LikeRepository likeRepository,
                              NotificationRepository notificationRepository,
                              NotificationEntityRepository notificationEntityRepository,
                              PersonToDtoMapper personToDtoMapper,
                              CommentToDTOMapper commentToDTOMapper) {
        this.personRepository = personRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.notificationRepository = notificationRepository;
        this.notificationEntityRepository = notificationEntityRepository;
        this.personToDtoMapper = personToDtoMapper;
        this.commentToDTOMapper = commentToDTOMapper;
    }

    public ResponseEntity<CommentResponse> addComment(Integer postID,
                                                      CommentBodyRequest commentRequest) throws NotFoundException, BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        Post post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        PostComment comment = new PostComment();

        comment.setTime(new Date());
        comment.setPost(post);

        if (commentRequest.getParent_id() != null) {
            PostComment parentComment = commentRepository.findCommentByID(commentRequest.getParent_id());
            comment.setParentComment(parentComment);
        } else {
            comment.setParentComment(null);
        }

        comment.setCommentText(commentRequest.getComment_text());
        var person = getCurrentUser();
        comment.setAuthor(person);
        comment.setDeleted(false);
        comment.setBlocked(false);

        var commentData = commentRepository.save(comment).getId();

        CommentDTO commentDTO = commentToDTOMapper.convertToDTO(comment);

        Runnable task = () -> {
            try {
                createNotificationEntity(commentData, person);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(task);
        thread.start();


        return ResponseEntity
                .ok(new CommentResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        commentDTO
                ));
    }

    public ResponseEntity<CommentsResponse> getCommentsByPostID(Integer postID) throws BadRequestException, NotFoundException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        List<PostComment> comments = commentRepository.findCommentsByPostID(postID);

        if (comments.isEmpty()) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        List<CommentDTO> commentDTOs = new ArrayList();

        comments.forEach(comment -> commentDTOs.add(commentToDTOMapper.convertToDTO(comment)));

        commentDTOs.forEach(commentDTO -> commentDTO.setLikes(commentRepository.getLikesOnComment(commentDTO.getId())));

        return ResponseEntity
                .ok(new CommentsResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        0,
                        0,
                        20,
                        commentDTOs
                ));
    }

    public ResponseEntity<CommentResponse> editComment(Integer commentID, CommentBodyRequest editCommentBody) throws BadRequestException, NotFoundException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        PostComment comment = commentRepository.findCommentByID(commentID);

        if (comment == null) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        comment.setCommentText(editCommentBody.getComment_text());

        commentRepository.save(comment);

        CommentDTO commentDTO = commentToDTOMapper.convertToDTO(comment);
        commentDTO.setLikes(commentRepository.getLikesOnComment(commentID));

        return ResponseEntity
                .ok(new CommentResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        commentDTO
                ));
    }

    public ResponseEntity<DeletePostByIDResponse> deleteComment(Integer commentID) throws BadRequestException, NotFoundException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        PostComment comment = commentRepository.findCommentByID(commentID);

        if (comment == null) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        comment.setDeleted(true);
        commentRepository.save(comment);

        PostDeleteDTO deleteDTO = new PostDeleteDTO();
        deleteDTO.setId(commentID);

        return ResponseEntity
                .ok(new DeletePostByIDResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        deleteDTO
                ));
    }

    public ResponseEntity<CommentResponse> recoverComment(Integer commentID) throws BadRequestException, NotFoundException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        PostComment comment = commentRepository.findCommentByID(commentID);

        if (comment == null) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        comment.setDeleted(false);

        CommentDTO commentDTO = commentToDTOMapper.convertToDTO(comment);
        commentDTO.setLikes(commentRepository.getLikesOnComment(commentID));
        return ResponseEntity
                .ok(new CommentResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        commentDTO
                ));
    }

    public ResponseEntity<ReportCommentResponse> reportComment(Integer commentID) throws BadRequestException, NotFoundException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        PostComment comment = commentRepository.findCommentByID(commentID);

        if (comment == null) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        //TODO отправляем id коммента куда-то

        return ResponseEntity
                .ok(new ReportCommentResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        new MessageDTO()
                ));
    }

    public ResponseEntity<IsLikedResponse> isLiked(Integer commentID) throws NotFoundException, BadRequestException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        Integer personID = getCurrentUser().getId();
        boolean isLiked = false;

        if (commentRepository.isUserLikedComment(personID, commentID) > 0) {
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


    public ResponseEntity<LikeResponse> addLike(Integer commentID) throws NotFoundException, BadRequestException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        PostLike like = new PostLike();

        like.setTime(new Date());
        like.setPerson(getCurrentUser());

        PostComment likedComment = commentRepository.findCommentByID(commentID);
        if (likedComment == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }
        like.setComment(likedComment);
        likeRepository.save(like);

        List<AuthorizedPerson> personDTOS = new ArrayList<>();

        List<Person> persons = likeRepository.getUsersWhoLikedComment(commentID);
        if (!persons.isEmpty()) {
            persons.forEach(person -> personDTOS.add(personToDtoMapper.convertToDto(person)));
        }

        LikeDTO likesDTO = new LikeDTO();
        likesDTO.setLikes(commentRepository.getLikesOnComment(commentID));
        likesDTO.setUsers(personDTOS);

        return ResponseEntity
                .ok(new LikeResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    public ResponseEntity<LikeResponse> deleteLike(Integer commentID) throws NotFoundException, BadRequestException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        Integer userID = getCurrentUser().getId();

        commentRepository.deleteLikeOnComment(userID, commentID);
        Integer likesCount = commentRepository.getLikesOnComment(commentID);

        LikeDTO likesDTO = new LikeDTO();
        likesDTO.setLikes(likesCount);

        return ResponseEntity
                .ok(new LikeResponse("successfully",
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    public ResponseEntity<LikeResponse> getAllLikes(Integer commentID) throws BadRequestException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        Integer likesCount = commentRepository.getLikesOnComment(commentID);
        List<Person> persons = likeRepository.getUsersWhoLikedComment(commentID);

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

    private void createNotificationEntity(Integer id, Person person) throws NotFoundException {
//      create new notification_entity
        var postComment = commentRepository.findCommentByID(id);
        if (!postComment.getPost().getAuthor().getId().equals(person.getId())) {
            NotificationEntity notificationEntity = new NotificationEntity();
            notificationEntity.setPerson(person);
            if (postComment.getParentComment() != null) {
                notificationEntity.setPostComment(postComment);
            }
            notificationEntity.setPost(postRepository.findPostByID(postComment.getPost().getId()));
            var notificationEnt = notificationEntityRepository.save(notificationEntity);
            ArrayList<Notification> postCommentArrayList = new ArrayList<>();
            var notification = new Notification();
            notification.setSentTime((Timestamp) postComment.getTime());
            notification.setEntity(notificationEnt);
            if (postComment.getParentComment() != null) {
                notification.setNotificationType(NotificationType.COMMENT_COMMENT);
                var parentComment = commentRepository.findCommentByID(postComment.getParentComment().getId());
                notification.setPerson(personRepository.findPersonById(parentComment.getAuthor().getId()));
                postCommentArrayList.add(notification);
            }
            notification.setNotificationType(NotificationType.POST_COMMENT);
            notification.setPerson(personRepository.findPersonById(postComment.getPost().getId()));
            postCommentArrayList.add(notification);
            notificationRepository.saveAll(postCommentArrayList);
        }
    }
}
