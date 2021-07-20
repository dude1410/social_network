package javapro.controller;



import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DefaultController {


    @GetMapping("/")
    public String index() {
        return "index";
    }


    @GetMapping(value = "policy.html")
    public String policy(){
        return "policy";
    }

    @GetMapping(value = "personal-data.html")
    public String personalData(){
        return "personal-data";
    }



    @RequestMapping(
            method = {RequestMethod.OPTIONS, RequestMethod.GET},
            value = "/*/{path:[^\\.]}")
    public String redirectToIndex(@PathVariable String path) {
        return "forward:/";
    }
}
