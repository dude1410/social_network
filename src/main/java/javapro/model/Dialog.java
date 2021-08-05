package javapro.model;

import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@Entity
@Table(name = "dialogs")
public class Dialog {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToMany(mappedBy = "dialog")
    private List<DialogMessage> dialogMessageList;

    @ManyToMany(cascade = CascadeType.ALL)
    @JoinTable(name = "dialog2person",
            joinColumns = {@JoinColumn(name = "id")},
            inverseJoinColumns = {@JoinColumn(name = "person_id")})
    private List<Person> personInDialog;
}
