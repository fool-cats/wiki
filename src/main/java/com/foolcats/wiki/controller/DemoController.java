package com.foolcats.wiki.controller;

import com.foolcats.wiki.domain.Demo;
import com.foolcats.wiki.service.DemoService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;
import java.util.List;

@RestController
@RequestMapping("/demo")
public class DemoController {

    @Resource
    private DemoService demoService;

    @RequestMapping("/list1")
    public List<Demo> list() {
        return demoService.list();
    }
}
