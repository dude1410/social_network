package JavaPRO.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.sql.Timestamp;

@Data
@AllArgsConstructor
public class OkResponse implements Response{


    private String error;

    private Timestamp timestamp;

    private ResponseData data;
}
