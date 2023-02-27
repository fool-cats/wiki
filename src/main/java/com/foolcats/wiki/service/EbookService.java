package com.foolcats.wiki.service;

import com.foolcats.wiki.domain.Ebook;
import com.foolcats.wiki.domain.EbookExample;
import com.foolcats.wiki.mapper.EbookMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
public class EbookService {
    @Resource
    private EbookMapper ebookMapper;

    public List<Ebook> list(String name){
        EbookExample ebookExample = new EbookExample();
//        createCriteria 相当于where
        EbookExample.Criteria criteria = ebookExample.createCriteria();
//        使用%模糊查询
        criteria.andNameLike("%" + name + "%");
        return ebookMapper.selectByExample(ebookExample);
    };

}
