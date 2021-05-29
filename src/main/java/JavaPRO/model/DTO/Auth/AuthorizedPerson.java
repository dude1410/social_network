package JavaPRO.model.DTO.Auth;


import JavaPRO.model.DTO.Country;
import JavaPRO.model.City;
import JavaPRO.model.ENUM.MessagesPermission;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.util.Date;


@Data
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class AuthorizedPerson {

    @JsonProperty(value = "id")
    private Integer id;

    @JsonProperty(value = "first_name")
    private String firstName;

    @JsonProperty(value = "last_name")
    private String lastName;

    @JsonProperty(value = "reg_date")
    private Date regDate;

    @JsonProperty(value = "birth_date")
    private Date birthDate;

    @JsonProperty(value = "email")
    private String email;

    @JsonProperty(value = "phone")
    private String phone;

    @JsonProperty(value = "photo")
    private String photo;

    @JsonProperty(value = "about")
    private String about;

    @JsonProperty(value = "town")
    private City city;

    @JsonProperty(value = "country")
    private Country country;

    @JsonProperty(value = "messages_permission")
    private MessagesPermission messagesPermission;

    @JsonProperty(value = "last_online_time")
    private Date lastOnlineTime;

    @JsonProperty(value = "is_blocked")
    private boolean isBlocked;

//    @JsonProperty(value = "token")
//    private String token = "1q2e3e3r4t5";

    @JsonProperty(value = "password")
    private String password;


}
