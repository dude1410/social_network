package JavaPRO.model.DTO;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Date;

public class UserLoginResponse {
    @JsonProperty(value = "id")
    private Long id;
    @JsonProperty(value = "first_name")
    private String firstName;
    @JsonProperty(value = "last_name")
    private String lastName;
    @JsonProperty(value = "reg_date")
    private Long regDate;
    @JsonProperty(value = "birth_date")
    private Long birthDate;
    @JsonProperty(value = "email")
    private String email;
    @JsonProperty(value = "phone")
    private Long phone;
    @JsonProperty(value = "photo")
    private String photo;
    @JsonProperty(value = "about")
    private String about;
    @JsonProperty(value = "city")
    private City city;
    @JsonProperty(value = "country")
    private Country country;
    @JsonProperty(value = "messages_permission")
    private String messagesPermission;
    @JsonProperty(value = "last_online_time")
    private Long lastOnlineTime;
    @JsonProperty(value = "is_blocked")
    private boolean isBlocked;
    @JsonProperty(value = "token")
    private String token = "1q2e3e3r4t5";

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
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

    public Long getRegDate() {
        return regDate;
    }

    public void setRegDate(Long regDate) {
        this.regDate = regDate;
    }

    public Long getBirthDate() {
        return birthDate;
    }

    public void setBirthDate(Long birthDate) {
        this.birthDate = birthDate;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Long getPhone() {
        return phone;
    }

    public void setPhone(Long phone) {
        this.phone = phone;
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

    public City getCity() {
        return city;
    }

    public void setCity(City city) {
        this.city = city;
    }

    public Country getCountry() {
        return country;
    }

    public void setCountry(Country country) {
        this.country = country;
    }

    public String getMessagesPermission() {
        return messagesPermission;
    }

    public void setMessagesPermission(String messagesPermission) {
        this.messagesPermission = messagesPermission;
    }

    public Long getLastOnlineTime() {
        return lastOnlineTime;
    }

    public void setLastOnlineTime(Long lastOnlineTime) {
        this.lastOnlineTime = lastOnlineTime;
    }

    public boolean isBlocked() {
        return isBlocked;
    }

    public void setBlocked(boolean blocked) {
        isBlocked = blocked;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }
}
