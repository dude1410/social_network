package javapro.model;

import javapro.model.view.PostCommentView;
import javapro.model.view.PostView;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

@Setter
@Getter
@Entity
@Table(name = "notification_entity")
public class NotificationEntity implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne
    @JoinColumn(name = "post_id", foreignKey = @ForeignKey(name = "FK_post_id"))
    private PostView postView;

    @ManyToOne
    @JoinColumn(name = "post_comment_id", foreignKey = @ForeignKey(name = "FK_post_comment_id"))
    private PostCommentView postComment;

    @ManyToOne
    @JoinColumn(name = "person_id", foreignKey = @ForeignKey(name = "FK_person_id"))
    private Person person;

    @ManyToOne
    @JoinColumn(name = "message_id", foreignKey = @ForeignKey(name = "FK_message_id"))
    private Message message;

}
