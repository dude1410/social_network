package javapro.model.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PersonDTO {
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
    @JsonProperty(value = "town")
    private TownDTO town;
    @JsonProperty(value = "country")
    private CountryDTO country;
    @JsonProperty(value = "messages_permission")
    private String messagesPermission;
    @JsonProperty(value = "last_online_time")
    private Long lastOnlineTime;
    @JsonProperty(value = "is_blocked")
    private boolean isBlocked;
}
