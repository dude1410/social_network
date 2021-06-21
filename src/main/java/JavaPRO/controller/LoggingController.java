package JavaPRO.controller;


import JavaPRO.model.DTO.LogModel.Logs;
import org.apache.commons.io.FileUtils;
import org.springframework.context.annotation.Profile;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoggingController {


    @Profile("dev")
    @GetMapping("/app/devlogs")
    public String logginDev(Model model) throws IOException{
        File file = new File("logs/rootLogs.log");
        model.addAttribute("logsList", getListLog(file));
        return "logs";
    }

    @Profile("prod")
    @GetMapping("/app/prodlogs")
    public String logginProd(Model model) throws IOException {
        File file = new File("/home/javapro/javapro-socialnetwork-studygroup-12/logs/rootLogs.log");
        model.addAttribute("logsList", getListLog(file));
        return "logs";
    }


    private ArrayList<Logs> getListLog(File file) throws IOException {
        List<String> logFile = FileUtils.readLines(file, StandardCharsets.UTF_8);
        ArrayList<Logs> logsList = new ArrayList<>();
        for (int i = 0; i < logFile.size(); i++) {
            Logs l = new Logs();
            l.setLogString(logFile.get(i));
            logsList.add(l);
        }
        return logsList;
    }
}