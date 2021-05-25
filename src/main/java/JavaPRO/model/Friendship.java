package JavaPRO.model;

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

    @Column(name = "status_id")
    private int statusId;

    @Column(name = "src_person_id")
    private int srcPersonId;

    @Column(name = "dst_person_id")
    private int dstPersonId;

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

    public int getStatusId() {
        return statusId;
    }

    public void setStatusId(int statusId) {
        this.statusId = statusId;
    }

    public int getSrcPersonId() {
        return srcPersonId;
    }

    public void setSrcPersonId(int srcPersonId) {
        this.srcPersonId = srcPersonId;
    }

    public int getDstPersonId() {
        return dstPersonId;
    }

    public void setDstPersonId(int dstPersonId) {
        this.dstPersonId = dstPersonId;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }
}
