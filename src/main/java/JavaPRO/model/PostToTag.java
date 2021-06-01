package JavaPRO.model;

import lombok.Data;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Data
@Entity
@Table(name = "post2tag")
public class PostToTag implements Serializable {

    @EmbeddedId
    private PostTagPK id;


}