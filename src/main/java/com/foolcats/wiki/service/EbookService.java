package com.foolcats.wiki.service;

import com.foolcats.wiki.domain.Ebook;
import com.foolcats.wiki.domain.EbookExample;
import com.foolcats.wiki.mapper.EbookMapper;
import com.foolcats.wiki.req.EbookReq;
import com.foolcats.wiki.req.EbookSaveReq;
import com.foolcats.wiki.resp.EbookQueryResp;
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
public class EbookService {

    private static final Logger LOG = LoggerFactory.getLogger(EbookService.class);

    @Resource
    private EbookMapper ebookMapper;

    @Resource
    private SnowFlake snowFlake;
    public PageResp<EbookQueryResp> list(EbookReq req){



        EbookExample ebookExample = new EbookExample();
//        createCriteria 相当于where
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())){
            //        使用%模糊查询
            criteria.andNameLike("%" + req.getName() + "%");
        }



//        只需一句就可实现后端分页。
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条相当于sql，
        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);
//        PageHelper只会对第一条sql分页，后面的sql语句不会分页，在使用的时候要小心。
//        ebookList = ebookMapper.selectByExample(ebookExample);

        PageInfo<Ebook> pageInfo = new PageInfo<>(ebookList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

//        将Ebook实体转换成EbookResp
//        List<EbookResp> respList = new ArrayList<>();





//         for (Ebook ebook : ebookList) {
////              EbookResp ebookResp = new EbookResp();
////              BeanUtils.copyProperties(ebook, ebookResp);
//             // 对象复制
//             EbookResp ebookResp = CopyUtil.copy(ebook, EbookResp.class);
//
//             respList.add(ebookResp);
//         }




//        for (Ebook ebook : respList) {
//            EbookResp ebookResp = new EbookResp();
////            下面的方法需要一个个字段的set，spring提供了BeanUtils节省,实现了对象复制
////            ebookResp.setId(ebook.getId());
//
//            BeanUtils.copyProperties(ebook,ebookResp);
//            respList.add(ebookResp);
//        }



//              列表复制
        List<EbookQueryResp>  list = CopyUtil.copyList(ebookList, EbookQueryResp.class);

        PageResp<EbookQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    };

    /*
    * 保存前端改变的值
    * */
    public void save(EbookSaveReq req){
//        将请求参数变为实体类Ebook
        Ebook ebook = CopyUtil.copy(req,Ebook.class);
//        保存分为新增保存和更新保存。
        if(!ObjectUtils.isEmpty(req.getId())){
            ebookMapper.updateByPrimaryKey(ebook);
        }else {

            ebook.setId(snowFlake.nextId());
            ebookMapper.insert(ebook);
        }

    }
}
