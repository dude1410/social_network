package javapro.model;


import com.fasterxml.jackson.annotation.JsonFormat;
import javapro.model.enums.ReadStatus;
import lombok.Getter;
import lombok.Setter;
import org.jetbrains.annotations.NotNull;
import org.springframework.format.annotation.DateTimeFormat;
import javax.persistence.*;
import java.util.Date;

@Getter
@Setter
@Entity
@Table(name = "dialog_message")
public class DialogMessage implements Comparable<DialogMessage>{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false)
    private int id;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "time", nullable = false)
    private Date time;

    @ManyToOne
    @JoinColumn(name = "dialog_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dialog_message_id"))
    private Dialog dialog;

    @ManyToOne
    @JoinColumn(name = "author_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dialog_author_id"))
    private Person authorId;

    @ManyToOne
    @JoinColumn(name = "recipient_id", nullable = false, foreignKey = @ForeignKey(name = "fk_dialog_recipient_id"))
    private Person recipientId;

    @Column(name = "message_text", nullable = false)
    private String messageText;

    @Enumerated(EnumType.STRING)
    @Column(name = "read_status", nullable = false)
    private ReadStatus readStatus;


    @Override
    public int compareTo(@NotNull DialogMessage o) {
        return time.compareTo(o.getTime());
    }
}
