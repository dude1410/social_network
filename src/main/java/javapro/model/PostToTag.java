package javapro.model;

import lombok.Data;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;
import java.io.Serializable;

@Getter
@Setter
@Entity
@Table(name = "post2tag")
public class PostToTag implements Serializable {

    @EmbeddedId
    private PostTagPK id;


}