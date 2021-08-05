package javapro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@Setter
@Entity
@Table(name = "dialog2person")
public class Dialog2person {

    @EmbeddedId
    private DialogPersonPK id;
}
