package com.shop.service;

import com.shop.pojo.Category;
import com.shop.pojo.vo.CategoryVO;
import com.shop.pojo.vo.NewItemsVO;

import java.util.List;

public interface CategoryService {

    /**
     * 查询一级分类
     * @return
     */
    public List<Category> queryAllRootLevelCat();

    /**
     * 根据一级分类查询子分类
     * @param rootCatId
     * @return
     */
    public List<CategoryVO> getSubCatList(Integer rootCatId);

    /**
     * 查询首页每个一级分类下的6条最新商品数据
     * @param rootCatId
     * @return
     */
    public List<NewItemsVO> getSixNewItemsLazy(Integer rootCatId);

}
