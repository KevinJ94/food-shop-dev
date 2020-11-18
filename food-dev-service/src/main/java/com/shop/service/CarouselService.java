package com.shop.service;

import com.shop.pojo.Carousel;

import java.util.List;

public interface CarouselService {
    /**
     * 查询所有轮播图
     * @param isShow
     * @return
     */
    public List<Carousel> queryAll(Integer isShow);

}
