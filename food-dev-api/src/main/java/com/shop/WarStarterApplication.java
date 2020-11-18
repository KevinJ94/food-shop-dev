package com.shop;

import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

//打包war[4] 添加war启动类
public class WarStarterApplication extends SpringBootServletInitializer {
    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder builder) {
        //指向Application springboot启动类

        return builder.sources(Application.class);
    }
}
