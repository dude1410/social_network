package javapro.services;

import javapro.api.response.PlatformResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.util.ArrayList;

@Service
public class NotificationService {

    public ResponseEntity<PlatformResponse> getNotification(Long offset, Long itemPerPage) {
        offset = (offset == null) ? 0 : offset;
        itemPerPage = (itemPerPage == null) ? 20 : itemPerPage;

        var response = new PlatformResponse();

        response.setError("ok");
        response.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        response.setTotal(0);
        response.setOffset(Math.toIntExact(offset));
        response.setPerPage(Math.toIntExact(itemPerPage));
        response.setData(new ArrayList());
        return ResponseEntity.ok(response);
    }


}
