package javapro.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@Data
@RequiredArgsConstructor
public class Response<T> {
    private String error;
    private Long timestamp;
    private T data;

    public Response(String ok, long time, FileStorageResponse fileStorageResponse) {
    }
}
