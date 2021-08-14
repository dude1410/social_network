package javapro.services;

import javapro.api.request.TagRequest;
import javapro.model.enums.NotificationType;
import javapro.model.view.PostView;
import javapro.repository.*;
import javapro.api.request.PostDataRequest;
import javapro.api.response.*;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.config.exception.NotFoundException;
import javapro.model.*;
import javapro.model.dto.*;
import javapro.util.PostToDtoCustomMapper;
import javapro.util.Time;
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
public class PostService {
    private final PersonRepository personRepository;
    private final PostViewRepository postRepository;
    private final TagRepository tagRepository;

    private final DeletedPersonRepository deletedPersonRepository;
    private final NotificationEntityRepository notificationEntityRepository;
    private final NotificationRepository notificationRepository;

    private final PostToDtoCustomMapper postToDTOCustomMapper;

    public PostService(PersonRepository personRepository,
                       PostViewRepository postRepository,
                       TagRepository tagRepository,
                       DeletedPersonRepository deletedPersonRepository,
                       NotificationEntityRepository notificationEntityRepository,
                       NotificationRepository notificationRepository) {
        this.personRepository = personRepository;
        this.postRepository = postRepository;
        this.tagRepository = tagRepository;
        this.deletedPersonRepository = deletedPersonRepository;
        this.notificationEntityRepository = notificationEntityRepository;
        this.notificationRepository = notificationRepository;
        this.postToDTOCustomMapper = Mappers.getMapper(PostToDtoCustomMapper.class);
    }

    public ResponseEntity<WallResponse> getPostsByUser(Integer personID,
                                                       Integer offset,
                                                       Integer itemPerPage) throws BadRequestException {

        if (personID == null) {
            throw new BadRequestException(Config.STRING_NO_USER_ID);
        }

        Person person = personRepository.findPersonById(personID);

        if (deletedPersonRepository.findByPersonId(person.getId()).isPresent()) {
            return ResponseEntity
                    .ok(new WallResponse("successfully",
                            Time.getTime(),
                            0,
                            0,
                            0,
                            null
                    ));
        }

        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);

        Page<PostView> postList = postRepository.findPostsByAuthorID(person.getId(), pageable);

        List<PostDTO> postDTOList = new ArrayList<>();

        postList.forEach(post -> postDTOList.add(postToDTOCustomMapper.mapper(post)));

        return ResponseEntity
                .ok(new WallResponse("successfully",
                        Time.getTime(),
                        (int) postList.getTotalElements(),
                        offset,
                        itemPerPage,
                        postDTOList
                ));
    }

    public ResponseEntity<PostResponse> getAllPosts(Integer offset, Integer itemPerPage) throws NotFoundException {


        Pageable pageable = PageRequest.of(offset / itemPerPage, itemPerPage);

        Page<PostView> postList = postRepository.findAllPosts(new Date(), pageable);

        List<PostDTO> postDTOList = new ArrayList<>();

        postList.forEach(post -> postDTOList.add(postToDTOCustomMapper.mapper(post)));

        return ResponseEntity
                .ok(new PostResponse("successfully",
                        Time.getTime(),
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

        PostView post = postRepository.findPostByID(postID);

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
                        Time.getTime(),
                        postDeleteDTO
                ));
    }

    public ResponseEntity<PostShortResponse> updatePostByID(Integer postID,
                                                            PostDataRequest postDataRequest) throws NotFoundException,
            BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        PostView post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }


        post.setPostText(postDataRequest.getPost_text());
        post.setTitle(postDataRequest.getTitle());
        post.setPostTagList(createTagsIfNew(postDataRequest.getTags()));

        postRepository.save(post);

        PostDTO postDTO = postToDTOCustomMapper.mapper(post);

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        Time.getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<PostShortResponse> getPostByID(Integer postID) throws NotFoundException, BadRequestException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        PostView post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        PostDTO postDTO = postToDTOCustomMapper.mapper(post);

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        Time.getTime(),
                        postDTO
                ));
    }

    // создание поста
    public ResponseEntity<PostShortResponse> publishPost(Long publishDate,
                                                         PostDataRequest postDataRequest) throws NotFoundException {
        Person currentUser = getCurrentUser();

        PostView post = new PostView();

        if (publishDate == null) {
            post.setTime(new Timestamp(System.currentTimeMillis()));
        } else {
            post.setTime(new Timestamp(publishDate));
        }
        post.setPostText(postDataRequest.getPost_text());
        post.setTitle(postDataRequest.getTitle());
        post.setBlocked(false);
        post.setAuthor(currentUser);
        post.setDeleted(false);
        post.setPostTagList(createTagsIfNew(postDataRequest.getTags()));

        PostView newPost = postRepository.save(post);

        PostDTO postDTO = postToDTOCustomMapper.mapper(post);

//      create notifications
        Runnable task = () -> {
            try {
                createNotificationEntity(newPost, currentUser);
            } catch (Exception e) {
                e.printStackTrace();
            }
        };
        Thread thread = new Thread(task);
        thread.start();

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        Time.getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<PostShortResponse> recoverPost(Integer postID) throws BadRequestException, NotFoundException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        PostView post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        postRepository.save(post);

        PostDTO postDTO = postToDTOCustomMapper.mapper(post);

        return ResponseEntity
                .ok(new PostShortResponse("successfully",
                        Time.getTime(),
                        postDTO
                ));
    }

    public ResponseEntity<ReportCommentResponse> reportPost(Integer postID) throws BadRequestException, NotFoundException {

        if (postID == null) {
            throw new BadRequestException(Config.STRING_NO_POST_ID);
        }

        PostView post = postRepository.findPostByID(postID);

        if (post == null) {
            throw new NotFoundException(Config.STRING_NO_POST_IN_DB);
        }

        //отправляем id поста куда-то

        return ResponseEntity
                .ok(new ReportCommentResponse("successfully",
                        Time.getTime(),
                        new MessageDTO()
                ));
    }

    //this method creates tags when it's new
    private List<Tag> createTagsIfNew(List<TagRequest> tagsRequest){
        List<Tag> tags = new ArrayList<>();
        if (tagsRequest != null) {

            for (TagRequest tagRequest : tagsRequest) {

                String tagName = tagRequest.getTag();

                Tag tag = tagRepository.findTagByName(tagName);

                if (tag == null) {
                    Tag newTag = new Tag();
                    newTag.setTag(tagName);
                    tags.add(tagRepository.save(newTag));
                } else {
                    tags.add(tag);
                }
            }
        }
        return tags;
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

    private void createNotificationEntity(PostView postView, Person person) throws NotFoundException {
//      create new notification_entity
        NotificationEntity notificationEntity = new NotificationEntity();
        notificationEntity.setPostView(postView);
        notificationEntity.setPerson(person);
        var notificationEnt = notificationEntityRepository.save(notificationEntity);

        var personList = activePerson(person.getId());
//      create new notifications for all person
        var notificationList = new ArrayList<Notification>();
        if (!personList.isEmpty()) {
            for (Person element : personList) {
                var notification = new Notification();
                notification.setSentTime((Timestamp) postView.getTime());
                notification.setNotificationType(NotificationType.POST);
                notification.setEntity(notificationEnt);
                notification.setPerson(element);
                notification.setInfo(postView.getTitle());
                notificationList.add(notification);
            }
            notificationRepository.saveAll(notificationList);
        }


    }

    //    find all isApproved, nonBlocked, nondeleted person
    private ArrayList<Person> activePerson(Integer authorId) throws NotFoundException {
        var personList = new ArrayList<Person>();
        var deletedList = deletedPersonRepository.findAll();
        var person = personRepository.findAllByisApprovedTrueAndisBlockedTrue();

        if (person.isEmpty()) {
            throw new NotFoundException(Config.STRING_BAD_REQUEST);
        }

        for (Person element : person) {
            int marker = 0;
            for (DeletedPerson deleteElement : deletedList) {
                if (element.getId() == deleteElement.getPersonId()) {
                    marker++;
                }
            }
            if (marker == 0) {
                if(!element.getId().equals(authorId)){
                    personList.add(element);
                }
            }
        }
        return personList;
    }

}