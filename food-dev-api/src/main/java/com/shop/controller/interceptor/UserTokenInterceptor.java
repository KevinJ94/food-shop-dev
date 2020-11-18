package com.shop.controller.interceptor;

import com.shop.utils.JSONResult;
import com.shop.utils.JsonUtils;
import com.shop.utils.RedisOperator;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.OutputStream;

public class UserTokenInterceptor implements HandlerInterceptor {

    @Autowired
    public RedisOperator redisOperator;

    public static final String REDIS_USER_TOKEN = "redis_user_token";
    /**
     * 拦截请求, 在controller之前
     * @param request
     * @param response
     * @param handler
     * @return
     * @throws Exception
     */
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
//
//        System.out.println("进入到拦截器");
        String userId = request.getHeader("headerUserId");
        String userToken = request.getHeader("headerUserToken");

        if(StringUtils.isNotBlank(userId) && StringUtils.isNotBlank(userToken)){
            //System.out.println(REDIS_USER_TOKEN + ":" + userId);
            String uniqueToken = redisOperator.get(REDIS_USER_TOKEN + ":" + userId);
            if(StringUtils.isBlank(uniqueToken)){
                returnErrorResponse(response,JSONResult.errorMsg("未找到uniqueToken, 请登录..."));
                return false;
            }else {
                if(!uniqueToken.equals(userToken)){
                    returnErrorResponse(response,JSONResult.errorMsg("账号在异地登录..."));
                    return false;
                }
            }
        }else {

            returnErrorResponse(response,JSONResult.errorMsg("参数为空, 请登录..."));
            return false;
        }


        return true;
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

    /**
     * 请求controller之后, 渲染视图之前
     * @param request
     * @param response
     * @param handler
     * @param modelAndView
     * @throws Exception
     */
    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler, ModelAndView modelAndView) throws Exception {

    }

    /**
     * 请求controller之后, 渲染视图之后
     * @param request
     * @param response
     * @param handler
     * @param ex
     * @throws Exception
     */
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {

    }
}
