package javapro.model;

import javapro.model.enums.MessagesPermission;
import javapro.model.enums.Role;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Timestamp;

@Setter
@Getter
@Entity
@Table(name = "person",
        uniqueConstraints =
        @UniqueConstraint(columnNames = {"e_mail"}, name = "uq_email")
)
public class Person implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "reg_date", nullable = false)
    private Timestamp regDate;

    @Column(name = "birth_date")
    private Timestamp birthDate;

    @Column(name = "e_mail", nullable = false)
    private String email;

    @Column(name = "phone")
    private String phone;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "photo", length = 1000)
    private String photo;

    @Column(name = "about", length = 1000)
    private String about;

    @ManyToOne
    @JoinColumn(name = "town_id", foreignKey = @ForeignKey(name = "FK_town_id"))
    private Town townId;

    @ManyToOne
    @JoinColumn(name = "country_id", foreignKey = @ForeignKey(name = "FK_country_id"))
    private Country countryId;

    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @Enumerated(EnumType.STRING)
    @Column(name = "messages_permission")
    @JoinColumn(name = "messages_permission", nullable = false)
    private MessagesPermission messagesPermission;

    @Column(name = "last_online_time", nullable = false)
    private Timestamp lastOnlineTime;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "role", nullable = false)
    private Integer role;

    @OneToOne(mappedBy = "person")
    private Token token;

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
