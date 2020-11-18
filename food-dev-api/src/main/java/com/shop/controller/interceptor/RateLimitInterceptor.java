package com.shop.controller.interceptor;

import com.google.common.util.concurrent.RateLimiter;
import com.shop.utils.JSONResult;
import com.shop.utils.JsonUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

@Slf4j
public class RateLimitInterceptor implements HandlerInterceptor {

    public static final int PERMITS_PER_SECOND = 10;

    RateLimiter limiter = RateLimiter.create(PERMITS_PER_SECOND);

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        if (limiter.tryAcquire(1)) {

            return true;
        } else {
            log.warn("Request failed, too many connections, current rate is {}", limiter.getRate());
            returnErrorResponse(response,JSONResult.errorMsg("请求过多, 已被限流..."));
            return false;
        }
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }

    public void returnErrorResponse(HttpServletResponse response,
                                    JSONResult result) {
        OutputStream out = null;
        try {
            response.setCharacterEncoding("utf-8");
            response.setContentType("text/json");
            out = response.getOutputStream();
            out.write(JsonUtils.objectToJson(result).getBytes("utf-8"));
            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
