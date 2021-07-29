package javapro.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import javapro.model.enums.NotificationType;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "notification")
public class Notification implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;


    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "notification_type", nullable = false)
    private NotificationType notificationType;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "sent_time", nullable = false)
    private Timestamp sentTime;

    @ManyToOne
    @JoinColumn(name = "person_id", nullable = false, foreignKey = @ForeignKey(name = "FK_person_id"))
    private Person person;

    @OneToOne
    @JoinColumn(name = "entity_id", nullable = false, foreignKey = @ForeignKey(name = "FK_entity_id"))
    private NotificationEntity entity;


}
