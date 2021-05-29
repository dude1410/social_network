package JavaPRO.controller;

import io.swagger.annotations.ApiOperation;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@Controller
public class DefaultController {

    @ApiOperation(value = "Получение настроек")
    @GetMapping("/")
    public String index() {
        return "redirect:/swagger-ui.html";
    }


    @RequestMapping(
            method = {RequestMethod.OPTIONS, RequestMethod.GET},
            value = "/*/{path:[^\\.]}")
    public String redirectToIndex(@PathVariable String path) {
        return "forward:/";
    }
}
