package javapro.util.storage;


import javapro.config.Config;
import javassist.NotFoundException;
import org.apache.commons.io.FileUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;


public class FileStorage {

    public void fileWriter(MultipartFile file, String originalImagePath) throws NotFoundException {
        byte[] originalImage;
        if (file == null) {
            throw new NotFoundException(Config.STRING_BAD_REQUEST);
        }
        try {
            originalImage = file.getBytes();
            try {
                File originalImageFile = new File(originalImagePath + file.getOriginalFilename());
                FileUtils.writeByteArrayToFile(originalImageFile, originalImage);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fileDelete(String fileFromDb, String imagePath) {
        FileUtils.deleteQuietly(FileUtils
                .getFile(imagePath + fileFromDb.substring(fileFromDb.lastIndexOf('/') + 1)));
    }
}
