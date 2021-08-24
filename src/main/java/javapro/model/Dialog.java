package javapro.model;

import lombok.Getter;
import lombok.Setter;
import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import org.jetbrains.annotations.NotNull;

@Getter
@Setter
@Entity
@Table(name = "dialogs")
public class Dialog implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToMany(mappedBy = "dialog")
    private List<DialogMessage> dialogMessageList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dialog2person",
            joinColumns = {@JoinColumn(name = "dialog_id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")})
    private List<Person> personInDialog;
}
