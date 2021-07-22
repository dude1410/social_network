package javapro.api.response;

import com.fasterxml.jackson.annotation.JsonProperty;
import javapro.model.Country;
import javapro.model.Town;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class ProfileByIdData {

    private Integer id;
    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("reg_date")
    private Long regDate;
    @JsonProperty("birth_date")
    private Long birthDate;
    private String email;
    private String phone;
    private String photo;
    private String about;
    private Town town;
    private Country country;
    @JsonProperty("message_permission")
    private String messagePermission;
    @JsonProperty("last_online_time")
    private Long lastOnlineTime;
    @JsonProperty("is_blocked")
    private Boolean isBlocked;
}
