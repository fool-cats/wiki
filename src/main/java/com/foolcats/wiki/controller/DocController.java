package com.foolcats.wiki.controller;

import com.foolcats.wiki.req.DocQueryReq;
import com.foolcats.wiki.req.DocSaveReq;
import com.foolcats.wiki.resp.CommonResp;
import com.foolcats.wiki.resp.DocQueryResp;
import com.foolcats.wiki.resp.PageResp;
import com.foolcats.wiki.service.DocService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/doc")
public class DocController {

    @Resource
    private DocService docService;

//    分页接口
    @RequestMapping("/list")
    public CommonResp list(@Valid DocQueryReq req) {
        CommonResp<PageResp<DocQueryResp>> resp = new CommonResp<>();
//        添加分页参数
        PageResp<DocQueryResp> list = docService.list(req);
        resp.setContent(list);
        return resp;
    }

    /*
    * 一次性返回所有分类，前面的/list接口每次会执行两次sql，算成总条数，然后是当前页。对于大系统来说影响性能。
    * 所以新增/all接口*/
//  查询所有，不分类。
    @RequestMapping("/all")
    public CommonResp all() {
        CommonResp<List<DocQueryResp>> resp = new CommonResp<>();
        List<DocQueryResp> list = docService.all();
        resp.setContent(list);
        return resp;
    }


//    保存的请求参数跟表结构一样。
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody DocSaveReq req) {
//        不需要返回任何东西给前端，只需true or false
//        CommonResp<PageResp<DocResp>> resp = new CommonResp<>();
        CommonResp resp = new CommonResp();
//        PageResp<DocResp> list =
                docService.save(req);

//        resp.setContent(list);
//        System.out.println("保存");
        return resp;
//        System.out.println("保存");
    }


    @DeleteMapping("/delete/{idsStr}")
    public CommonResp delete(@PathVariable String idsStr) {
//      删除一般是通过id来删除的，因为id是主键
        CommonResp resp = new CommonResp<>();
//        PageResp<DocResp> list =
//        字符串转数组
        List<String> list = Arrays.asList(idsStr.split(","));
        docService.delete(list);

//        resp.setContent(list);
        return resp;
    }
}
