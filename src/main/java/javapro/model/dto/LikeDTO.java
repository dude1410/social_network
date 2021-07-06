package javapro.model.dto;

import javapro.model.dto.auth.AuthorizedPerson;
import lombok.Data;

import java.util.List;

@Data
public class LikeDTO {
    Integer likes;

    List<AuthorizedPerson> users;
}
