package javapro.model;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "person_token")
public class Token {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @OneToOne
    @JoinColumn(name = "person_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_person_token_id"))
    private Person person;

    @Column(nullable = false)
    private String token;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH:mm:ss")
    @Column(name= "creating_date", nullable = false)
    private Date date;
}
