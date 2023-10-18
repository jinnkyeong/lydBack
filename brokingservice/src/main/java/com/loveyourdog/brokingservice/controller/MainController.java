package com.loveyourdog.brokingservice.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

//@RestController
@Controller
public class MainController {

    // 바로 스웨거 화면 열기+자동 기록
    @GetMapping("/api/swagger/swagger")
    public String redirectSwagger() {
        return "redirect:/swagger-ui/index.html";
    }

}
