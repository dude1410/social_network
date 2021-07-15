package javapro.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

import java.sql.Timestamp;

@Data
@RequiredArgsConstructor
public class EditMyProfileRequest {

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("birth_date")
    private Timestamp birthDate;
    private String phone;
    @JsonProperty("photo_id")
    private Long photoId;
    private String about;
    @JsonProperty("town_id")
    private Integer townId;
    @JsonProperty("country_id")
    private Integer countryId;
}
