package javapro.controller;

import javapro.api.response.Response;
import javapro.services.StorageService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

@RestController
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }

    @PostMapping(value = "/api/v1/storage")
    public ResponseEntity<Response> storage(MultipartFile file) throws Exception {
        return storageService.fileStore(file);
    }
}
