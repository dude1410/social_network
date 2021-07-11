package javapro.util.storage;


import javapro.config.Config;
import javassist.NotFoundException;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.nio.file.Path;
import java.util.Objects;


public class FileStorage {

    public void fileWriter(MultipartFile file, String originalImagePath, String thumbImagePath) throws NotFoundException {
        BufferedImage originalImage;
        BufferedImage thumbImage;

        if (file == null) {
            throw new NotFoundException(Config.STRING_BAD_REQUEST);
        }
        try {
            originalImage = ImageIO.read(file.getInputStream());
            thumbImage = Scalr.resize(originalImage,
                    Scalr.Method.QUALITY,
                    Scalr.Mode.FIT_TO_WIDTH,
                    128,
                    Scalr.OP_ANTIALIAS);
            if (!Files.exists(Path.of(originalImagePath))) {
                Files.createDirectories(Path.of(originalImagePath));
            }

            String origFile = originalImagePath + file.getOriginalFilename();
            var originalImg = new File(originalImagePath + "/" + file.getOriginalFilename());
            if (!Files.exists(Path.of(origFile))) {
                ImageIO.write(originalImage, Objects.requireNonNull(file.getOriginalFilename())
                        .substring(file.getOriginalFilename()
                                .lastIndexOf('.') + 1), originalImg);
            }
            String thumbFile = originalImagePath + "/thumb/" + file.getOriginalFilename();
            var thumbImg = new File(thumbFile + "/thumb/" + file.getOriginalFilename());
            if (!Files.exists(Path.of(String.valueOf(thumbImg)))) {
                ImageIO.write(thumbImage, Objects.requireNonNull(file.getOriginalFilename())
                        .substring(file.getOriginalFilename()
                                .lastIndexOf('.') + 1), thumbImg);
            }
            System.out.println(thumbImg.getAbsolutePath());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
