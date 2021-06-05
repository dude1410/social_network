package JavaPRO.controller;


import org.apache.commons.io.FileUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@RestController
public class LoggingController {


    @GetMapping("/app/logs")
    public List<String> login() throws IOException {
        File file = new File("logs/logger.log");
        List logs = FileUtils.readLines(file, StandardCharsets.UTF_8);
        return logs;
    }
}