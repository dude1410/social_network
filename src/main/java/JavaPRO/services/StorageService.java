package JavaPRO.services;

import JavaPRO.Util.Storage.FileStorage;
import JavaPRO.api.response.FileStorageResponse;
import JavaPRO.api.response.Response;
import JavaPRO.config.Config;
import JavaPRO.config.exception.AuthenticationException;
import JavaPRO.config.exception.BadRequestException;
import JavaPRO.model.DTO.Storage;
import JavaPRO.repository.PersonRepository;
import JavaPRO.repository.StorageRepository;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Path;
import java.sql.Timestamp;
import java.time.LocalDate;

@Service
public class StorageService {

    @Value("${javapro.storagepath}")
    private String uploadPath;

    private final Logger logger = LogManager.getLogger(StorageService.class);


    private final PersonRepository personRepository;
    private final StorageRepository storageRepository;

    public StorageService(PersonRepository personRepository,
                          StorageRepository storageRepository) {
        this.personRepository = personRepository;
        this.storageRepository = storageRepository;
    }


    public ResponseEntity<Response> fileStore(MultipartFile file) throws BadRequestException,
            NotFoundException,
            AuthenticationException {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }
        FileStorage fileStorage = new FileStorage();
        if (file == null) {
            throw new BadRequestException(Config.STRING_BAD_REQUEST);
        }
        var person = personRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());

        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }


        String originalImagePath = uploadPath + "/storage/" + LocalDate.now().toString() + "/" + person.getId().toString() + "/";
        String thumbImagePath = uploadPath + "/storage/" + LocalDate.now().toString() + "/" + person.getId().toString() + "/";
        String relative = new File(uploadPath).toURI().relativize(new File(thumbImagePath).toURI()).getPath();
        person.setPhoto(relative);
        personRepository.save(person);

        Runnable task = () -> {
            try {
                if (!Files.exists(Path.of(originalImagePath))) {
                    Files.createDirectories(Path.of(originalImagePath));
                }
                fileStorage.fileWriter(file, originalImagePath, thumbImagePath);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        };
        Thread thread = new Thread(task);
        thread.start();

        Storage storage = new Storage();

        storage.setOriginal(originalImagePath + file.getOriginalFilename());
        storage.setThumb(thumbImagePath + "thumb" + file.getOriginalFilename());
        var photoId= storageRepository.save(storage).getId();


        FileStorageResponse fileStorageResponse = new FileStorageResponse();
        fileStorageResponse.setOwnerId(person.getId());
        fileStorageResponse.setBytes(file.getSize());
        fileStorageResponse.setFileFormat(file.getContentType());
        fileStorageResponse.setFileName(file.getOriginalFilename());
        fileStorageResponse.setCreatedAt(new Timestamp(System.currentTimeMillis()).getTime());
        fileStorageResponse.setFileType(file.getContentType());
        fileStorageResponse.setRawFileURL(thumbImagePath + "thumb" + file.getOriginalFilename());
        fileStorageResponse.setRelativeFilePath(relative + "thumb" + file.getOriginalFilename());
        fileStorageResponse.setId(Math.toIntExact(photoId));

        var response = new Response();
        response.setError("ok");
        response.setTimestamp(new Timestamp(System.currentTimeMillis()).getTime());
        response.setData(fileStorageResponse);

        return ResponseEntity.ok(response);
    }
}
