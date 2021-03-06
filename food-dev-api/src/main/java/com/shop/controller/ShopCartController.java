package com.shop.controller;

import com.shop.pojo.bo.ShopcartBO;
import com.shop.utils.JSONResult;
import com.shop.utils.JsonUtils;
import com.shop.utils.RedisOperator;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.ArrayList;
import java.util.List;

@Api(
    value = "购物车接口",
    tags = {"购物车接口"})
@RequestMapping("shopcart")
@RestController
public class ShopCartController extends BaseController {

  @Autowired
  private RedisOperator redisOperator;

  @ApiOperation(value = "同步购物车", notes = "同步购物车", httpMethod = "POST")
  @PostMapping("/add")
  public JSONResult add(
      @RequestParam String userId,
      @RequestBody ShopcartBO shopcartBO,
      HttpServletRequest request,
      HttpServletResponse response) {
    if (StringUtils.isBlank(userId)) {
      return JSONResult.errorMsg("");
    }
    System.out.println(shopcartBO);
    // 前端用户在登录的情况下,添加商品到购物车,会同时在后端同步购物车带redis缓存
    // 需要判断当前购物车中包含已经存在的商品，如果存在则累加购买数量
    String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
    List<ShopcartBO> shopcartList = null;
    if (StringUtils.isNotBlank(shopcartJson)) {
      // redis中已经有购物车了
      shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
      // 判断购物车中是否存在已有商品，如果有的话counts累加
      boolean isHaving = false;
      for (ShopcartBO sc: shopcartList) {
        String tmpSpecId = sc.getSpecId();
        if (tmpSpecId.equals(shopcartBO.getSpecId())) {
          sc.setBuyCounts(sc.getBuyCounts() + shopcartBO.getBuyCounts());
          isHaving = true;
        }
      }
      if (!isHaving) {
        shopcartList.add(shopcartBO);
      }
    } else {
      // redis中没有购物车
      shopcartList = new ArrayList<>();
      // 直接添加到购物车中
      shopcartList.add(shopcartBO);
    }

    // 覆盖现有redis中的购物车
    redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
    return JSONResult.ok();
  }

  @ApiOperation(value = "从购物车中删除商品", notes = "从购物车中删除商品", httpMethod = "POST")
  @PostMapping("/del")
  public JSONResult del(
          @RequestParam String userId,
          @RequestParam String itemSpecId,
          HttpServletRequest request,
          HttpServletResponse response) {
    if (StringUtils.isBlank(userId) || StringUtils.isBlank(itemSpecId)) {
      return JSONResult.errorMsg("参数不能为空");
    }

    // 用户在页面删除购物车的数据,如果用户已经登陆,则需要同步删除后端购物车数据
    String shopcartJson = redisOperator.get(FOODIE_SHOPCART + ":" + userId);
    if (StringUtils.isNotBlank(shopcartJson)) {
      // redis中已经有购物车了
      List<ShopcartBO> shopcartList = JsonUtils.jsonToList(shopcartJson, ShopcartBO.class);
      // 判断购物车中是否存在已有商品，如果有的话则删除
      for (ShopcartBO sc: shopcartList) {
        String tmpSpecId = sc.getSpecId();
        if (tmpSpecId.equals(itemSpecId)) {
          shopcartList.remove(sc);
          break;
        }
      }
      // 覆盖现有redis中的购物车
      redisOperator.set(FOODIE_SHOPCART + ":" + userId, JsonUtils.objectToJson(shopcartList));
    }

    return JSONResult.ok();
  }

}
