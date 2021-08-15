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
import javapro.model.view.PostCommentView;
import javapro.model.view.PostView;
import javapro.repository.*;
import javapro.util.CommentToDTOCustomMapper;
import javapro.util.PersonToDtoMapper;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
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
    private final PostCommentViewRepository commentRepository;
    private final PostViewRepository postRepository;
    private final LikeRepository likeRepository;
    private final NotificationRepository notificationRepository;
    private final NotificationEntityRepository notificationEntityRepository;

    private final PersonToDtoMapper personToDtoMapper;
    private final CommentToDTOCustomMapper commentToDTOCustomMapper;

    public PostCommentService(PersonRepository personRepository,
                              PostCommentViewRepository commentRepository,
                              PostViewRepository postRepository,
                              LikeRepository likeRepository,
                              NotificationRepository notificationRepository,
                              NotificationEntityRepository notificationEntityRepository,
                              PersonToDtoMapper personToDtoMapper) {
        this.personRepository = personRepository;
        this.commentRepository = commentRepository;
        this.postRepository = postRepository;
        this.likeRepository = likeRepository;
        this.notificationRepository = notificationRepository;
        this.notificationEntityRepository = notificationEntityRepository;
        this.personToDtoMapper = personToDtoMapper;
        this.commentToDTOCustomMapper = Mappers.getMapper(CommentToDTOCustomMapper.class);
    }

    public ResponseEntity<CommentResponse> addComment(Integer postID,
                                                      CommentBodyRequest commentRequest) throws NotFoundException, BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        var post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        var comment = new PostCommentView();

        comment.setTime(new Date());
        comment.setPost(post);

        if (commentRequest.getParentID() != null) {
            var parentComment = commentRepository.findCommentByID(commentRequest.getParentID());
            comment.setParentComment(parentComment);
        } else {
            comment.setParentComment(null);
        }

        comment.setCommentText(commentRequest.getCommentText());
        var person = getCurrentUser();
        comment.setAuthor(person);
        comment.setDeleted(false);
        comment.setBlocked(false);

        var commentData = commentRepository.save(comment).getId();

        var commentDTO = commentToDTOCustomMapper.mapper(comment);

        Runnable task = () -> {
            try {
                createNotificationEntity(commentData, person);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        var thread = new Thread(task);
        thread.start();


        return ResponseEntity
                .ok(new CommentResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        commentDTO
                ));
    }

    public ResponseEntity<CommentsResponse> getCommentsByPostID(Integer postID, Integer offset, Integer itemPerPage) throws BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        var pageable = PageRequest.of(offset / itemPerPage, itemPerPage);

        Page<PostCommentView> comments = commentRepository.findCommentsByPostID(postID, pageable);

        List<CommentDTO> commentDTOs = new ArrayList();

        comments.forEach(comment -> commentDTOs.add(commentToDTOCustomMapper.mapper(comment)));

        return ResponseEntity
                .ok(new CommentsResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        (int) comments.getTotalElements(),
                        offset,
                        itemPerPage,
                        commentDTOs
                ));
    }

    public ResponseEntity<CommentResponse> editComment(Integer commentID, CommentBodyRequest editCommentBody) throws BadRequestException, NotFoundException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        var comment = commentRepository.findCommentByID(commentID);

        if (comment == null) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        comment.setCommentText(editCommentBody.getCommentText());

        commentRepository.save(comment);

        var commentDTO = commentToDTOCustomMapper.mapper(comment);

        return ResponseEntity
                .ok(new CommentResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        commentDTO
                ));
    }

    public ResponseEntity<DeletePostByIDResponse> deleteComment(Integer commentID) throws BadRequestException, NotFoundException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        var comment = commentRepository.findCommentByID(commentID);

        if (comment == null) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        comment.setDeleted(true);
        commentRepository.save(comment);

        var deleteDTO = new PostDeleteDTO();
        deleteDTO.setId(commentID);

        return ResponseEntity
                .ok(new DeletePostByIDResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        deleteDTO
                ));
    }

    public ResponseEntity<CommentResponse> recoverComment(Integer commentID) throws BadRequestException, NotFoundException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        var comment = commentRepository.findCommentByID(commentID);

        if (comment == null) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        comment.setDeleted(false);

        var commentDTO = commentToDTOCustomMapper.mapper(comment);

        return ResponseEntity
                .ok(new CommentResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        commentDTO
                ));
    }

    public ResponseEntity<ReportCommentResponse> reportComment(Integer commentID) throws BadRequestException, NotFoundException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        var comment = commentRepository.findCommentByID(commentID);

        if (comment == null) {
            throw new NotFoundException(Config.STRING_NO_COMMENT_IN_DB);
        }

        //отправляем id коммента куда-то

        return ResponseEntity
                .ok(new ReportCommentResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        new MessageDTO()
                ));
    }

    public ResponseEntity<IsLikedResponse> isLiked(Integer commentID) throws NotFoundException, BadRequestException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        var personID = getCurrentUser().getId();
        boolean isLiked = false;

        if (commentRepository.isUserLikedComment(personID, commentID) > 0) {
            isLiked = true;
        }

        var isLikedDTO = new IsLikedDTO();

        isLikedDTO.setLikes(isLiked);

        return ResponseEntity
                .ok(new IsLikedResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        isLikedDTO
                ));
    }


    public ResponseEntity<LikeResponse> addLike(Integer commentID) throws NotFoundException, BadRequestException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        var like = new PostLike();

        like.setTime(new Date());
        like.setPerson(getCurrentUser());

        var likedComment = commentRepository.findCommentByID(commentID);

        if (likedComment == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }
        like.setComment(likedComment);
        likeRepository.save(like);

        List<AuthorizedPerson> personDTOS = new ArrayList<>();

        var persons = likeRepository.getUsersWhoLikedComment(commentID);
        if (!persons.isEmpty()) {
            persons.forEach(person -> personDTOS.add(personToDtoMapper.convertToDto(person)));
        }

        var likesDTO = new LikeDTO();
        likesDTO.setLikes(commentRepository.getLikesOnComment(commentID));
        likesDTO.setUsers(personDTOS);

        return ResponseEntity
                .ok(new LikeResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    public ResponseEntity<LikeResponse> deleteLike(Integer commentID) throws NotFoundException, BadRequestException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        var userID = getCurrentUser().getId();

        commentRepository.deleteLikeOnComment(userID, commentID);
        var likesCount = commentRepository.getLikesOnComment(commentID);

        var likesDTO = new LikeDTO();
        likesDTO.setLikes(likesCount);

        return ResponseEntity
                .ok(new LikeResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    public ResponseEntity<LikeResponse> getAllLikes(Integer commentID) throws BadRequestException {

        if (commentID == null) {
            throw new BadRequestException(Config.STRING_NO_COMMENT_ID);
        }

        var likesCount = commentRepository.getLikesOnComment(commentID);
        List<Person> persons = likeRepository.getUsersWhoLikedComment(commentID);

        var likesDTO = new LikeDTO();

        likesDTO.setLikes(likesCount);

        if (likesCount > 0) {
            List<AuthorizedPerson> personDTOS = null;
            persons.forEach(person -> personDTOS.add(personToDtoMapper.convertToDto(person)));
            likesDTO.setUsers(personDTOS);
        }

        return ResponseEntity
                .ok(new LikeResponse(Config.WALL_RESPONSE,
                        new Timestamp(System.currentTimeMillis()).getTime(),
                        likesDTO
                ));
    }

    private Person getCurrentUser() throws NotFoundException {
        var personEmail = SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName();

        var person = personRepository.findByEmail(personEmail);

        if (person == null) {
            throw new NotFoundException(Config.STRING_NO_PERSON_IN_DB);
        }

        return person;
    }

    private void createNotificationEntity(Integer id, Person person)  {
//      create new notification_entity
        var postComment = commentRepository.findCommentByID(id);
        if (!postComment.getPost().getAuthor().getId().equals(person.getId())) {
            var notificationEntity = new NotificationEntity();
            notificationEntity.setPerson(person);
            if (postComment.getParentComment() != null) {
                notificationEntity.setPostComment(postComment);
            }
            var post = postRepository.findPostByID(postComment.getPost().getId());
            notificationEntity.setPost(post);
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
            notification.setPerson(personRepository.findPersonById(postComment.getPost().getAuthor().getId()));
            notification.setInfo(post.getTitle());
            postCommentArrayList.add(notification);
            notificationRepository.saveAll(postCommentArrayList);
        }
    }
}
