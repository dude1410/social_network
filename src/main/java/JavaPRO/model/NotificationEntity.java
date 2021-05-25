package JavaPRO.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notification_entity")
public class NotificationEntity  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "post_id", nullable = false)
    private int postId;

    @Column(name = "post_comment_id", nullable = false)
    private int postCommentId;

    @Column(name = "person_id", nullable = false)
    private int personId;

    @Column(name = "message_id", nullable = false)
    private int messageId;

//  One notification Many notification_entity ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//    @OneToMany(mappedBy = "notification_entity", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Notification> notifications = new HashSet<>();
//
//    public Set<Notification> getNotification() {
//        return notifications;
//    }
//
//    public boolean addNotification(Notification notification) {
//        notification.setEntityId(this);
//        return getNotification().add(notification);
//    }
//
//    public void removeNotification(Notification notification) {
//        getNotification().remove(notification);
//    }
//
//    public void setNotification(Set<Notification> notification) {
//        this.notifications = notification;
//    }

//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getPostId() {
        return postId;
    }

    public void setPostId(int postId) {
        this.postId = postId;
    }

    public int getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(int postCommentId) {
        this.postCommentId = postCommentId;
    }

    public int getPersonId() {
        return personId;
    }

    public void setPersonId(int personId) {
        this.personId = personId;
    }

    public int getMessageId() {
        return messageId;
    }

    public void setMessageId(int messageId) {
        this.messageId = messageId;
    }
}
