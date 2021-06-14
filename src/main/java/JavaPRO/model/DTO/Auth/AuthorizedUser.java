package JavaPRO.model.DTO.Auth;


import JavaPRO.model.DTO.City;
import JavaPRO.model.DTO.Country;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class AuthorizedUser {

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
}
