package JavaPRO.model;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "post2tag")
public class PostToTag {
/*
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;
    */
    @EmbeddedId
    private PostTagPK id;

    public PostTagPK getId() {
        return id;
    }

    public void setId(PostTagPK id) {
        this.id = id;
    }
}
