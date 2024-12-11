package me.holiday.test;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1")
public class TestApi {

    @GetMapping("/test")
    public String authTest() {
        return "GOOD";
    }
}
