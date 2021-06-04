package JavaPRO.model;

import lombok.Data;
import javax.persistence.*;
import java.io.Serializable;

@Data
@Embeddable
public class PostTagPK implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "post_id", nullable = false, foreignKey = @ForeignKey(name = "FK_post_id"))
    private Post post;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "tag_id", nullable = false, foreignKey = @ForeignKey(name = "FK_tag_id"))
    private Tag tag;

    public PostTagPK(Post post, Tag tag){
        this.post = post;
        this.tag = tag;
    }

    public PostTagPK() {
    }

    @Override
    public boolean equals(Object obj) {
        return (this == obj);
    }

    @Override
    public int hashCode() {
        return this.hashCode();
    }
}
