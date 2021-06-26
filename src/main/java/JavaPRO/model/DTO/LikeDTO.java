package JavaPRO.model.DTO;

import JavaPRO.model.DTO.Auth.AuthorizedPerson;
import lombok.Data;

import java.util.List;

@Data
public class LikeDTO {
    Integer likes;

    List<AuthorizedPerson> users;
}
