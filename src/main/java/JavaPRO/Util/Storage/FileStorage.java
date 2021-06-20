package JavaPRO.Util.Storage;


import JavaPRO.config.Config;
import javassist.NotFoundException;
import org.imgscalr.Scalr;
import org.springframework.web.multipart.MultipartFile;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.util.Objects;

public class FileStorage {

    public void fileWriter(MultipartFile file, String originalImagePath, String thumbImagePath) throws NotFoundException {
        BufferedImage originalImage;
        BufferedImage thumbImage;

        if (file == null) {
            throw new NotFoundException(Config.STRING_BAD_REQUEST);
        }
        try {
            var extension = Objects.requireNonNull(file.getOriginalFilename())
                    .substring(file.getOriginalFilename()
                            .lastIndexOf('.') + 1);
            originalImage = ImageIO.read(file.getInputStream());
            thumbImage = Scalr.resize(originalImage,
                    Scalr.Method.QUALITY,
                    Scalr.Mode.FIT_TO_WIDTH,
                    128,
                    Scalr.OP_ANTIALIAS);
            var thumbImg = new File(thumbImagePath);
            ImageIO.write(thumbImage, extension, thumbImg);
            var originalImg = new File(originalImagePath);
            ImageIO.write(originalImage, extension, originalImg);
        } catch (Exception e) {
            e.printStackTrace();
        }


    }
}
