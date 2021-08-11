package javapro.services;

import javapro.api.response.FileStorageResponse;
import javapro.api.response.Response;
import javapro.config.Config;
import javapro.config.exception.AuthenticationException;
import javapro.config.exception.BadRequestException;
import javapro.repository.PersonRepository;
import javapro.util.Time;
import javassist.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Paths;
import java.util.Objects;

@Service
public class StorageService {

    @Value("${javapro.storagepath}")
    private String uploadPath;

    @Value(("${javapro.storagepath.baseurl}"))
    private String baseURL;

    private final Logger logger = LogManager.getLogger(StorageService.class);


    private final PersonRepository personRepository;
    private final PasswordEncoder passwordEncoder;

    public StorageService(PersonRepository personRepository, PasswordEncoder passwordEncoder) {
        this.personRepository = personRepository;
        this.passwordEncoder = passwordEncoder;
    }


    public ResponseEntity<Response<FileStorageResponse>> fileStore(MultipartFile file) throws BadRequestException,
            NotFoundException,
            AuthenticationException {

        if (!SecurityContextHolder.getContext().getAuthentication().isAuthenticated()) {
            throw new AuthenticationException(Config.STRING_AUTH_ERROR);
        }

        if (file == null) {
            throw new BadRequestException(Config.STRING_BAD_REQUEST);
        }
        if (file.getSize() > 500000L) {
            throw new BadRequestException(Config.STRING_FILE_TOO_BIG);
        }
        var person = personRepository.findByEmail(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName());

        if (person == null) {
            throw new NotFoundException(Config.STRING_AUTH_LOGIN_NO_SUCH_USER);
        }


        var fileName = (passwordEncoder.encode(Objects.requireNonNull(file.getOriginalFilename()).split("\\.")[0]))
                .substring(8);
        var fileExtension = file.getOriginalFilename().substring(file.getOriginalFilename().lastIndexOf('.') + 1);
        var fullFileName = fileName + "." + fileExtension;
        var full = uploadPath + "/storage/thumb/" + fullFileName;
        String imagePath = uploadPath + "/storage/thumb/";
        String relative = "/" + new File(uploadPath).toURI().relativize(new File(imagePath).toURI()).getPath();

        var fileFromDb = person.getPhoto();
        if (fileFromDb != null) {
            FileUtils.deleteQuietly(FileUtils
                    .getFile(imagePath + fileFromDb.substring(fileFromDb.lastIndexOf('/') + 1)));
        }
        person.setPhoto(relative + file.getOriginalFilename());
        personRepository.save(person);

        try {
            file.transferTo(Paths.get(full));
        } catch (Exception e) {
            logger.error(e.getMessage());
        }


        FileStorageResponse fileStorageResponse = new FileStorageResponse();

        fileStorageResponse.setOwnerId(person.getId());
        fileStorageResponse.setBytes(file.getSize());
        fileStorageResponse.setFileFormat(file.getContentType());
        fileStorageResponse.setFileName(fullFileName);
        fileStorageResponse.setCreatedAt(Time.getTime());
        fileStorageResponse.setFileType(file.getContentType());
        fileStorageResponse.setRawFileURL(full);
        fileStorageResponse.setRelativeFilePath(relative  + fullFileName);

        Response<FileStorageResponse> response = new Response<>();
        response.setError("ok");
        response.setTimestamp(Time.getTime());
        response.setData(fileStorageResponse);
        return ResponseEntity.ok(response);
    }
}
