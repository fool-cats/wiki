package com.foolcats.wiki.service;

import com.foolcats.wiki.domain.Ebook;
import com.foolcats.wiki.domain.EbookExample;
import com.foolcats.wiki.mapper.EbookMapper;
import com.foolcats.wiki.req.EbookReq;
import com.foolcats.wiki.resp.EbookResp;
import com.foolcats.wiki.utils.CopyUtil;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<EbookResp> list(EbookReq req){
        EbookExample ebookExample = new EbookExample();
//        createCriteria 相当于where
        EbookExample.Criteria criteria = ebookExample.createCriteria();

        if(!ObjectUtils.isEmpty(req.getName())){
            //        使用%模糊查询
            criteria.andNameLike("%" + req.getName() + "%");
        }


        List<Ebook> ebookList = ebookMapper.selectByExample(ebookExample);



//        将Ebook实体转换成EbookResp
        List<EbookResp> respList = new ArrayList<>();





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
        List<EbookResp>  list = CopyUtil.copyList(ebookList, EbookResp.class);

        return list;
    };

}
