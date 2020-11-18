package com.shop.controller.center;

import com.shop.pojo.Users;
import com.shop.service.center.CenterUserService;
import com.shop.utils.JSONResult;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController
@Api(value = "用户中心",tags = {"用户中心展示的相关接口"})
@RequestMapping("center")
public class CenterController {

    @Autowired
    private CenterUserService centerUserService;

    @ApiOperation(value = "获取用户信息", notes = "获取用户信息",httpMethod = "GET")
    @GetMapping("userInfo")
    public JSONResult userInfo(
            @ApiParam(name = "userId")
            @RequestParam String userId){
        Users user = centerUserService.queryUserInfo(userId);
        return JSONResult.ok(user);

    }

}
