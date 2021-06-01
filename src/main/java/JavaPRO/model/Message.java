package JavaPRO.model;

import JavaPRO.model.ENUM.ReadStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@Entity
@Table(name = "message")
public class Message implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "time", nullable = false)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "FK_author_id"))
    private Person authorId;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false, foreignKey = @ForeignKey(name = "FK_recipient_id"))
    private Person recipientId;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", nullable = false)
    private ReadStatus readStatus;

}
