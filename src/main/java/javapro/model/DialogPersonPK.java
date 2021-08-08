package javapro.model;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@Embeddable
public class DialogPersonPK implements Serializable {

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dialog_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dialog2person_dialogs_id"))
    private Dialog dialog;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dialog2person_person_id"))
    private Person person;

}
