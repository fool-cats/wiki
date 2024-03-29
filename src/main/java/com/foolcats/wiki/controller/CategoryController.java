package com.foolcats.wiki.controller;

import com.foolcats.wiki.req.CategoryQueryReq;
import com.foolcats.wiki.req.CategorySaveReq;
import com.foolcats.wiki.resp.CategoryQueryResp;
import com.foolcats.wiki.resp.CommonResp;
import com.foolcats.wiki.resp.PageResp;
import com.foolcats.wiki.service.CategoryService;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.List;

@RestController
@RequestMapping("/category")
public class CategoryController {

    @Resource
    private CategoryService categoryService;

//    分页接口
    @RequestMapping("/list")
    public CommonResp list(@Valid CategoryQueryReq req) {
        CommonResp<PageResp<CategoryQueryResp>> resp = new CommonResp<>();
//        添加分页参数
        PageResp<CategoryQueryResp> list = categoryService.list(req);
        resp.setContent(list);
        return resp;
    }

    /*
    * 一次性返回所有分类，前面的/list接口每次会执行两次sql，算成总条数，然后是当前页。对于大系统来说影响性能。
    * 所以新增/all接口*/
//  查询所有，不分类。
    @RequestMapping("/all")
    public CommonResp all() {
        CommonResp<List<CategoryQueryResp>> resp = new CommonResp<>();
        List<CategoryQueryResp> list = categoryService.all();
        resp.setContent(list);
        return resp;
    }


//    保存的请求参数跟表结构一样。
    @PostMapping("/save")
    public CommonResp save(@Valid @RequestBody CategorySaveReq req) {
//        不需要返回任何东西给前端，只需true or false
//        CommonResp<PageResp<CategoryResp>> resp = new CommonResp<>();
        CommonResp resp = new CommonResp();
//        PageResp<CategoryResp> list =
                categoryService.save(req);

//        resp.setContent(list);
//        System.out.println("保存");
        return resp;
//        System.out.println("保存");
    }


    @DeleteMapping("/delete/{id}")
    public CommonResp delete(@PathVariable Long id) {
//      删除一般是通过id来删除的，因为id是主键
        CommonResp resp = new CommonResp();
//        PageResp<CategoryResp> list =
        categoryService.delete(id);

//        resp.setContent(list);
        return resp;
    }
}
