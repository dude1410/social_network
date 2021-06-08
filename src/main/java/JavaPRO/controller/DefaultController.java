package JavaPRO.controller;


import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

@Controller
public class DefaultController {

    @ApiOperation(value = "Получение настроек")
    @GetMapping("/")
    public String index() {
        return "index";
    }

    @GetMapping(value = "/policy.html")
    public String getPolicy(){
        return "policy";
    }

    @GetMapping(value = "/personal-data.html")
    public String getPersonalData(){
        return "personal-data";
    }

    @RequestMapping(
            method = {RequestMethod.OPTIONS, RequestMethod.GET},
            value = "/*/{path:[^\\.]}")
    public String redirectToIndex(@PathVariable String path) {
        return "forward:/";
    }
}
