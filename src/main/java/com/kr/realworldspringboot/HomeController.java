package com.kr.realworldspringboot;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HomeController {

    @RequestMapping("/test")
    public String helloWorld() {
        return "HelloWorld";
    }

    @RequestMapping("/aa")
    public String helloWorlda() {
        return "HelloWorld AAAA" ;
    }
}
