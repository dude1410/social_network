package JavaPRO.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Entity
@Table(name = "notification")
public class Notification  implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//    @ManyToOne
////    @JoinColumn(name = "type_id")
//    @JoinColumn(name = "type_id", nullable = false)
//    private NotificationType typeId;
//
//    public void setTypeId(NotificationType notificationTypes) {
//        this.typeId = notificationTypes;
//    }
//
//    public NotificationType getTypeId() {
//        return typeId;
//    }
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "sent_time", nullable = false)
    private Date sentTime;

//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//    @ManyToOne
////    @JoinColumn(name = "person_id")
//    @JoinColumn(name = "person_id", nullable = false)
//    private Person personId;
//
//    public void setPersonId(Person personId) {
//        this.personId = personId;
//    }
//
//    public Person getPersonId(){
//        return personId;
//    }
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

//↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//    @ManyToOne
////    @JoinColumn(name = "entity_id")
//    @JoinColumn(name = "entity_id", nullable = false)
//    private NotificationEntity entityId;
//
//    public NotificationEntity getEntityId() {
//        return entityId;
//    }
//
//    public void setEntityId(NotificationEntity entityId) {
//        this.entityId = entityId;
//    }
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Date getSentTime() {
        return sentTime;
    }

    public void setSentTime(Date sentTime) {
        this.sentTime = sentTime;
    }


}
