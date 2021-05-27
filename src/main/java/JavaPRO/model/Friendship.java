package JavaPRO.model;

import JavaPRO.model.ENUM.FriendshipStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "friendship")
public class Friendship  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "status_id")
    private FriendshipStatus status;

    @ManyToOne
    @JoinColumn(name = "src_person_id")
    private Person srcPersonId;

    @ManyToOne
    @JoinColumn(name = "dst_person_id")
    private Person dstPersonId;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "time")
    private Date time;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public FriendshipStatus getStatus() {
        return status;
    }

    public void setStatusId(FriendshipStatus status) {
        this.status = status;
    }

    public Person getSrcPersonId() {
        return srcPersonId;
    }

    public void setSrcPersonId(Person srcPersonId) {
        this.srcPersonId = srcPersonId;
    }

    public Person getDstPersonId() {
        return dstPersonId;
    }

    public void setDstPersonId(Person dstPersonId) {
        this.dstPersonId = dstPersonId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
