package javapro.model.view;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonManagedReference;
import javapro.model.Person;
import javapro.model.Post;
import javapro.model.PostComment;
import javapro.model.PostLike;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "vi_post_comment")
public class PostCommentView implements Serializable, Comparable<PostCommentView> {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "time", nullable = false)
    private Date time;

    @ManyToOne
    @JsonBackReference
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "FK_post_id"))
    private Post post;

    @ManyToOne
    @JoinColumn(name = "parent_id", foreignKey = @ForeignKey(name = "FK_parent_comment_id"))
    private PostComment parentComment;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "FK_author_id"))
    private Person author;

    @Column(name = "comment_text", nullable = false)
    private String commentText;

    @Column(name = "is_deleted", nullable = false)
    private boolean isDeleted;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @JsonManagedReference
    @OneToMany(mappedBy = "comment", cascade = CascadeType.ALL)
    private List<PostLike> commentLikeList;

    @Override
    public int compareTo(PostCommentView comment) {
        return comment.getTime().compareTo(this.getTime());
    }

}
