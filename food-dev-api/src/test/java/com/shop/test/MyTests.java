package com.shop.test;


import com.shop.Application;
import com.shop.utils.RedisOperator;
import lombok.extern.slf4j.Slf4j;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.redisson.api.RLock;
import org.redisson.api.RedissonClient;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;


import java.io.IOException;
import java.util.concurrent.TimeUnit;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = Application.class)
@Slf4j
public class MyTests {

    @Autowired
    RedissonClient redissonClient;

    @Autowired
    RedisOperator redisOperator;

    @Test
    public void contextLoads() {
    }

    @Test
    public void testRedis(){
        System.out.println(redisOperator.get("subCat:3"));
    }


    @Test
    public void testLock() throws IOException {

        RLock rLock = redissonClient.getLock("test_lock");
        try {

            rLock.lock(5, TimeUnit.HOURS);
            log.info("我获得了锁！！！");
            Thread.sleep(10000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }finally {
            rLock.unlock();
            log.info("我释放了锁！！");

        }

    }
}
