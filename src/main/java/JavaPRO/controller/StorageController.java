package JavaPRO.controller;

import JavaPRO.services.StorageService;
import org.springframework.stereotype.Controller;
import org.springframework.web.multipart.MultipartFile;

@Controller
public class StorageController {

    private final StorageService storageService;

    public StorageController(StorageService storageService) {
        this.storageService = storageService;
    }


    public Object storage(MultipartFile file) throws Exception {
        return storageService.fileStore(file);
    }
}
