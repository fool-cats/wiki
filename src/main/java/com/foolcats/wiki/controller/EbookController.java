package com.foolcats.wiki.controller;

import com.foolcats.wiki.req.EbookReq;
import com.foolcats.wiki.req.EbookSaveReq;
import com.foolcats.wiki.resp.CommonResp;
import com.foolcats.wiki.resp.EbookQueryResp;
import com.foolcats.wiki.resp.PageResp;
import com.foolcats.wiki.service.EbookService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

@RestController
@RequestMapping("/ebook")
public class EbookController {

    @Resource
    private EbookService ebookService;

    @RequestMapping("/list")
    public CommonResp list(EbookReq req) {
        CommonResp<PageResp<EbookQueryResp>> resp = new CommonResp<>();
        PageResp<EbookQueryResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }


//    保存的请求参数跟表结构一样。
    @PostMapping("/save")
    public CommonResp save(@RequestBody EbookSaveReq req) {
//        不需要返回任何东西给前端，只需true or false
//        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        CommonResp resp = new CommonResp();
//        PageResp<EbookResp> list =
                ebookService.save(req);

//        resp.setContent(list);
        return resp;
    }
}
