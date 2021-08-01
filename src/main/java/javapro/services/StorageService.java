package javapro.services;

import javapro.api.response.FileStorageResponse;
import javapro.api.response.Response;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.repository.PersonRepository;
import javapro.util.Time;
import javapro.util.storage.FileStorage;
import javassist.NotFoundException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;

@Service
public class StorageService {

    @Value("${javapro.storagepath}")
    private String uploadPath;

    @Value("${javapro.storagepath.baseurl}")
    private String baseUrl;

    private final Logger logger = LogManager.getLogger(StorageService.class);


    private final PersonRepository personRepository;

    public StorageService(PersonRepository personRepository) {
        this.personRepository = personRepository;
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

        String originalImagePath = uploadPath; // + "/storage/"; //+ LocalDate.now().toString() + "/" + person.getId().toString() + "/";
        String thumbImagePath = uploadPath; //+ "/storage/thumb/";//+ LocalDate.now().toString() + "/" + person.getId().toString() + "/thumb/";
        String relative = "/" + new File(uploadPath).toURI().relativize(new File(thumbImagePath).toURI()).getPath();


        person.setPhoto(baseUrl + relative + file.getOriginalFilename());
        personRepository.save(person);
        Runnable task = () -> {
            try {
                fileStorage.fileWriter(file, originalImagePath);
            } catch (Exception e) {
                logger.error(e.getMessage());
            }
        };
        Thread thread = new Thread(task);
        thread.start();


        FileStorageResponse fileStorageResponse = new FileStorageResponse();
        fileStorageResponse.setOwnerId(person.getId());
        fileStorageResponse.setBytes(file.getSize());
        fileStorageResponse.setFileFormat(file.getContentType());
        fileStorageResponse.setFileName(file.getOriginalFilename());
        fileStorageResponse.setCreatedAt(Time.getTime());
        fileStorageResponse.setFileType(file.getContentType());
        fileStorageResponse.setRawFileURL(thumbImagePath + file.getOriginalFilename());
        fileStorageResponse.setRelativeFilePath(relative + "/" + file.getOriginalFilename());

        System.out.println(thumbImagePath);
        System.out.println(relative);
        Response response = new Response();
        response.setError("ok");
        response.setTimestamp(Time.getTime());
        response.setData(fileStorageResponse);
        return ResponseEntity.ok(response);
    }
}
