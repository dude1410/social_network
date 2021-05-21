package JavaPRO.model;

import JavaPRO.model.ENUM.Role;

import javax.persistence.*;
import java.util.Date;
import java.util.Objects;

@Entity
@Table(name = "person")
public class Person {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id", nullable = false, unique = true)
    private Integer id;

    @Column(name = "first_name", nullable = false)
    private String firstName;

    @Column(name = "last_name", nullable = false)
    private String lastName;

    @Column(name = "reg_date", nullable = false)
    private Date regDate;

    @Column(name = "birth_date")
    private Date birthDate;

    @Column(name = "e_mail", nullable = false, unique = true)
    private String email;

    @Column(name = "password", nullable = false)
    private String password;

    @Column(name = "photo")
    private String photo;

    @Column(name = "about")
    private String about;

    @Column(name = "town_id")
    private String town;

    @Column(name = "country_id")
    private String country;


    @Column(name = "confirmation_code")
    private String confirmationCode;

    @Column(name = "is_approved", nullable = false)
    private boolean isApproved;

    @Column(name = "message_permission_id", nullable = false)
    private Integer messagePermissionId;

    @Column(name = "last_online_time", nullable = false)
    private Date lastOnlineTime;

    @Column(name = "is_blocked", nullable = false)
    private boolean isBlocked;

    @Column(name = "role", nullable = false)
    private Integer role;


    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Date getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Date birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }

    public String getAbout() {
        return about;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    public String getTown() {
        return town;
    }

    public void setTown(String town) {
        this.town = town;
    }

    public String getConfirmationCode() {
        return confirmationCode;
    }

    public void setConfirmationCode(String confirmationCode) {
        this.confirmationCode = confirmationCode;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }

    public Integer getMessagePermissionId() {
        return messagePermissionId;
    }

    public void setMessagePermissionId(Integer messagePermissionId) {
        this.messagePermissionId = messagePermissionId;
    }

    public Date getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Date lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

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
