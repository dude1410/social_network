package JavaPRO.api.response;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public abstract class Response<T> {

    private String error;

    private Long timestamp;

    private T data;
}
