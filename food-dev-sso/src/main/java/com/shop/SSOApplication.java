package com.shop;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import tk.mybatis.spring.annotation.MapperScan;

@SpringBootApplication
@MapperScan(basePackages = "com.shop.mapper")
@ComponentScan(basePackages = {"com.shop", "org.n3r.idworker"})
public class SSOApplication {

    public static void main(String[] args) {
        SpringApplication.run(SSOApplication.class, args);
    }

}
