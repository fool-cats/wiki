package com.foolcats.wiki.controller;

import com.foolcats.wiki.req.EbookReq;
import com.foolcats.wiki.resp.CommonResp;
import com.foolcats.wiki.resp.EbookResp;
import com.foolcats.wiki.resp.PageResp;
import com.foolcats.wiki.service.EbookService;
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
        CommonResp<PageResp<EbookResp>> resp = new CommonResp<>();
        PageResp<EbookResp> list = ebookService.list(req);
        resp.setContent(list);
        return resp;
    }
}
