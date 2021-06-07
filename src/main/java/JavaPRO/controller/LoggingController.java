package JavaPRO.controller;


import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Controller
public class LoggingController {


    @GetMapping("/app/logs")
    public List<String> login() throws IOException {
        File file = new File("logs/logger.log");
        List logs = FileUtils.readLines(file, StandardCharsets.UTF_8);
        return logs;
    }
}