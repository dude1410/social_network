package javapro.model;

import javapro.model.enums.FriendshipStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "friendship")
public class Friendship implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @Column(name = "status")
    private FriendshipStatus status;

    @ManyToOne
    @JoinColumn(name = "src_person_id", foreignKey = @ForeignKey(name = "FK_src_person_id"))
    private Person srcPersonId;

    @ManyToOne
    @JoinColumn(name = "dst_person_id", foreignKey = @ForeignKey(name = "FK_dst_person_id"))
    private Person dstPersonId;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "time")
    private Date time;

}
