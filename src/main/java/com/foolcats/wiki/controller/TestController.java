package com.foolcats.wiki.controller;

import com.foolcats.wiki.domain.Test;
import com.foolcats.wiki.service.TestService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
public class TestController {

    @Resource
    private TestService testService;
    @RequestMapping("/hello")
    public String hello() {
        return "Hello";
    }

    @PostMapping("/hello/post")
    public String helloPost(String name) {
        return "Hello Post" + name;
    }


    @RequestMapping("/test/list")
    public List<Test> list() {
        return testService.list();
    }
}
