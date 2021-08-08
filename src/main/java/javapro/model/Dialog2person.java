package javapro.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
@Table(name = "dialog2person")
public class Dialog2person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "dialog_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dialog2person_dialogs_id"))
    private Dialog dialog;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dialog2person_person_id"))
    private Person person;
}
