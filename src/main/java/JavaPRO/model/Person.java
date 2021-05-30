package JavaPRO.model;

import JavaPRO.model.ENUM.MessagesPermission;
import JavaPRO.model.ENUM.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "person")
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "first_name", nullable = false, length = 255)
    private String firstName;

    @Column(name = "last_name", nullable = false, length = 255)
    private String lastName;

    @DateTimeFormat(pattern = "yyyy.MM.dd HH-mm")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd HH-mm")
    @Column(name = "reg_date", nullable = false)
    private Date regDate;

    @DateTimeFormat(pattern = "yyyy.MM.dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy.MM.dd")
    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "e_mail", nullable = false, unique = true, length = 255)
    private String email;

    @Column(name = "phone", length = 255)
    private String phone;

    @Column(name = "password", nullable = false, length = 255)
    private String password;

    @Column(name = "photo", length = 1000)
    private String photo;

    @Column(name = "about", length = 1000)
    private String about;

    @ManyToOne
    @JoinColumn(name = "town_id")
    private Town townId;

    @ManyToOne
    @JoinColumn(name = "country_id")
    private Country countryId;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @Enumerated(EnumType.STRING)
    @JoinColumn(name = "messages_permission", nullable = false)
    private MessagesPermission messagesPermission;

    @Column(name = "last_online_time", nullable = false)
    private Date lastOnlineTime;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "role", nullable = false)
    private Integer role;

    public Role getRole() {
        if (role == 0) {
            return Role.USER;
        }
        if (role == 1) {
            return Role.MODDERATOR;
        }
        return Role.ADMINISTRATOR;
    }

    public void setRole(Integer role) {
        this.role = role;
    }

}
