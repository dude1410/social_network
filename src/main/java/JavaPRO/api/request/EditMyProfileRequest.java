package JavaPRO.api.request;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class EditMyProfileRequest {

    @JsonProperty("first_name")
    private String firstName;
    @JsonProperty("last_name")
    private String lastName;
    @JsonProperty("birth_date")
    private String birthDate;
    private String phone;
    @JsonProperty("photo_id")
    private Long photoId;
    private String about;
    @JsonProperty("city")
    private String townId;
    @JsonProperty("country")
    private String countryId;
}
