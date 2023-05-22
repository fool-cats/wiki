package com.foolcats.wiki.service;

import com.foolcats.wiki.domain.Category;
import com.foolcats.wiki.domain.CategoryExample;
import com.foolcats.wiki.mapper.CategoryMapper;
import com.foolcats.wiki.req.CategoryQueryReq;
import com.foolcats.wiki.req.CategorySaveReq;
import com.foolcats.wiki.resp.CategoryQueryResp;
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
public class CategoryService {

    private static final Logger LOG = LoggerFactory.getLogger(CategoryService.class);

    @Resource
    private CategoryMapper categoryMapper;

    @Resource
    private SnowFlake snowFlake;
    public List<CategoryQueryResp> all(){
        CategoryExample categoryExample = new CategoryExample();
//        根据sort排序。
        categoryExample.setOrderByClause("sort asc");
//        这条相当于sql，
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);

//              列表复制
        List<CategoryQueryResp>  list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        return list;
    };


//    分页接口

    public PageResp<CategoryQueryResp> list(CategoryQueryReq req){



        CategoryExample categoryExample = new CategoryExample();
        categoryExample.setOrderByClause("sort asc");
//        createCriteria 相当于where
        CategoryExample.Criteria criteria = categoryExample.createCriteria();


//        只需一句就可实现后端分页。
        PageHelper.startPage(req.getPage(), req.getSize());
//        这条相当于sql，
        List<Category> categoryList = categoryMapper.selectByExample(categoryExample);
//        PageHelper只会对第一条sql分页，后面的sql语句不会分页，在使用的时候要小心。
//        categoryList = categoryMapper.selectByExample(categoryExample);

        PageInfo<Category> pageInfo = new PageInfo<>(categoryList);
        LOG.info("总行数:{}",pageInfo.getTotal());
        LOG.info("总页数:{}",pageInfo.getPages());

//        将Category实体转换成CategoryResp
//        List<CategoryResp> respList = new ArrayList<>();





//         for (Category category : categoryList) {
////              CategoryResp categoryResp = new CategoryResp();
////              BeanUtils.copyProperties(category, categoryResp);
//             // 对象复制
//             CategoryResp categoryResp = CopyUtil.copy(category, CategoryResp.class);
//
//             respList.add(categoryResp);
//         }




//        for (Category category : respList) {
//            CategoryResp categoryResp = new CategoryResp();
////            下面的方法需要一个个字段的set，spring提供了BeanUtils节省,实现了对象复制
////            categoryResp.setId(category.getId());
//
//            BeanUtils.copyProperties(category,categoryResp);
//            respList.add(categoryResp);
//        }



//              列表复制
        List<CategoryQueryResp>  list = CopyUtil.copyList(categoryList, CategoryQueryResp.class);

        PageResp<CategoryQueryResp> pageResp = new PageResp<>();
        pageResp.setTotal(pageInfo.getTotal());
        pageResp.setList(list);
        return pageResp;
    };


    /*
    * 保存前端改变的值
    * */
    public void save(CategorySaveReq req){
//        将请求参数变为实体类Category
        Category category = CopyUtil.copy(req,Category.class);
//        保存分为新增保存和更新保存。
        if(!ObjectUtils.isEmpty(req.getId())){
            categoryMapper.updateByPrimaryKey(category);
        }else {

            category.setId(snowFlake.nextId());
            categoryMapper.insert(category);
        }

    }

    public void delete(Long id) {
        categoryMapper.deleteByPrimaryKey(id);
    }
}
