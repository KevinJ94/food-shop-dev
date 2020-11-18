package com.shop.service;


import com.shop.utils.PagedGridResult;

public interface ItemsESService {

    public PagedGridResult searhItems(String keywords,
                                      String sort,
                                      Integer page,
                                      Integer pageSize);

}
