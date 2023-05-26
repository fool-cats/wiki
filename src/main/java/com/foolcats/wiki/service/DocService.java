package com.foolcats.wiki.service;

import com.foolcats.wiki.domain.Doc;
import com.foolcats.wiki.domain.DocExample;
import com.foolcats.wiki.mapper.DocMapper;
import com.foolcats.wiki.req.DocQueryReq;
import com.foolcats.wiki.req.DocSaveReq;
import com.foolcats.wiki.resp.DocQueryResp;
import com.foolcats.wiki.resp.PageResp;
import com.foolcats.wiki.utils.CopyUtil;
import com.foolcats.wiki.utils.SnowFlake;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.List;

@Service
public class DocService {

    private static final Logger LOG = LoggerFactory.getLogger(DocService.class);

    @Resource
    private DocMapper docMapper;

    @Resource
    private SnowFlake snowFlake;
    public List<DocQueryResp> all(){
        DocExample docExample = new DocExample();
//        根据sort排序。
        docExample.setOrderByClause("sort asc");
//        这条相当于sql，
        List<Doc> docList = docMapper.selectByExample(docExample);

//              列表复制
        List<DocQueryResp>  list = CopyUtil.copyList(docList, DocQueryResp.class);

        return list;
    };


//    分页接口

    public PageResp<DocQueryResp> list(DocQueryReq req){



        DocExample docExample = new DocExample();
        docExample.setOrderByClause("sort asc");
//        createCriteria 相当于where
        DocExample.Criteria criteria = docExample.createCriteria();


//        只需一句就可实现后端分页。
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条相当于sql，
        List<Doc> docList = docMapper.selectByExample(docExample);
//        PageHelper只会对第一条sql分页，后面的sql语句不会分页，在使用的时候要小心。
//        docList = docMapper.selectByExample(docExample);

        PageInfo<Doc> pageInfo = new PageInfo<>(docList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

//        将Doc实体转换成DocResp
//        List<DocResp> respList = new ArrayList<>();





//         for (Doc doc : docList) {
////              DocResp docResp = new DocResp();
////              BeanUtils.copyProperties(doc, docResp);
//             // 对象复制
//             DocResp docResp = CopyUtil.copy(doc, DocResp.class);
//
//             respList.add(docResp);
//         }




//        for (Doc doc : respList) {
//            DocResp docResp = new DocResp();
////            下面的方法需要一个个字段的set，spring提供了BeanUtils节省,实现了对象复制
////            docResp.setId(doc.getId());
//
//            BeanUtils.copyProperties(doc,docResp);
//            respList.add(docResp);
//        }



//              列表复制
        List<DocQueryResp>  list = CopyUtil.copyList(docList, DocQueryResp.class);

        PageResp<DocQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    };


    /*
    * 保存前端改变的值
    * */
    public void save(DocSaveReq req){
//        将请求参数变为实体类Doc
        Doc doc = CopyUtil.copy(req,Doc.class);
//        保存分为新增保存和更新保存。
        if(!ObjectUtils.isEmpty(req.getId())){
            docMapper.updateByPrimaryKey(doc);
        }else {

            doc.setId(snowFlake.nextId());
            docMapper.insert(doc);
        }

    }

    public void delete(Long id) {
        docMapper.deleteByPrimaryKey(id);
    }
}
