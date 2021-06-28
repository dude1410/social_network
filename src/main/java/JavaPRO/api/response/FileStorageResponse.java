package JavaPRO.api.response;

import lombok.Data;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Data
public class FileStorageResponse {

    private int id;

    private int ownerId;

    private String fileName;

    private String relativeFilePath;

    private String rawFileURL;

    private String fileFormat;

    private Long bytes;

    private String fileType;

    private Long createdAt;

}
