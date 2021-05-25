package JavaPRO.model;

import javax.persistence.*;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "notification_type")
public class NotificationType  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @Column(name = "name", nullable = false)
    private String postId;

    @Column(name = "code", nullable = false)
    private String postCommentId;

//  One notification Many notification_type ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//    @OneToMany(mappedBy = "notification_type", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Notification> notifications = new HashSet<>();
//
//    public Set<Notification> getNotification() {
//        return notifications;
//    }
//
//    public boolean addNotification(Notification notification) {
//        notification.setTypeId(this);
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

    public String getPostId() {
        return postId;
    }

    public void setPostId(String postId) {
        this.postId = postId;
    }

    public String getPostCommentId() {
        return postCommentId;
    }

    public void setPostCommentId(String postCommentId) {
        this.postCommentId = postCommentId;
    }
}
