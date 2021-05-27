package JavaPRO.model;

import JavaPRO.model.ENUM.MessagesPermission;
import JavaPRO.model.ENUM.Role;
import com.fasterxml.jackson.annotation.JsonFormat;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "person")
public class Person {

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

    //  One notification Many person ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//    @OneToMany(mappedBy = "person")
//    private Set<Notification> notifications = new HashSet<>();
//
//    public Set<Notification> getNotifications() {
//        return notifications;
//    }
//
//    public boolean addNotification(Notification notification) {
//        notification.setPersonId(this);
//        return getNotifications().add(notification);
//    }
//
//    public void removeNotification(Notification notification) {
//        getNotifications().remove(notification);
//    }
//
//    public void setNotifications(Set<Notification> notifications) {
//        this.notifications = notifications;
//    }
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

//  One user Many post ↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓↓
//    @OneToMany(mappedBy = "person", cascade = CascadeType.ALL, orphanRemoval = true)
//    private Set<Post> posts = new HashSet<>();
//
//    public Set<Post> getPosts() {
//        return posts;
//    }
//
//    public boolean addPost(Post post) {
//        post.setAuthorId(this);
//        return getPosts().add(post);
//    }
//
//    public void removePost(Post post) {
//        getPosts().remove(post);
//    }
//
//    public void setPosts(Set<Post> posts) {
//        this.posts = posts;
//    }
//↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑↑

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

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
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

    public Town getTownId() {
        return townId;
    }

    public void setTownId(Town townId) {
        this.townId = townId;
    }

    public Country getCountryId() {
        return countryId;
    }

    public void setCountryId(Country countryId) {
        this.countryId = countryId;
    }

    public MessagesPermission getMessagesPermission() {
        return messagesPermission;
    }

    public void setMessagesPermission(MessagesPermission messagesPermission) {
        this.messagesPermission = messagesPermission;
    }
}
