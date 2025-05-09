package com.efren.springcloud.msvc.hospital.config;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class WebController implements ErrorController {

    @GetMapping("/")
    public String index() {
        return "forward:/index.html";
    }

    @RequestMapping("/error")
    public String handleError() {
        return "error"; // Debes tener error.html en templates/
    }
}
