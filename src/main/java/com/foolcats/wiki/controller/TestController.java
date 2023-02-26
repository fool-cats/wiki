package com.foolcats.wiki.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class TestController {

    @RequestMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "Hello Post" + name;
    }
}
