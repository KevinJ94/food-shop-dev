package com.shop.controller;

import com.shop.utils.RedisOperator;
import io.swagger.annotations.Api;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import springfox.documentation.annotations.ApiIgnore;

@RestController()
@Api(value = "redis测试", tags = {"redis测试"})
@RequestMapping("redis")
public class RedisController {

    @Autowired
    private RedisOperator redisOperator;

    @GetMapping("/set")
    public void set(@RequestParam String key, @RequestParam String value){
        redisOperator.set(key,value);

    }

    @GetMapping("/get")
    public String get(@RequestParam String key){
        return redisOperator.get(key);
    }

    @GetMapping("/delete")
    public Object delete(@RequestParam String key){
        redisOperator.del(key);
        return "OK";
    }
}
