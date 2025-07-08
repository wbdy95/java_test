package com.xiaobai.project_data.interceptor;

import com.alibaba.fastjson.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    // 简单的API Key，可以在这里修改
    private static final String VALID_API_KEY = "6c2879442b1e863d82d97bd5e60cdd62";

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取请求头中的API Key
        String apiKey = request.getHeader("api-key");

        // 验证API Key
        if (VALID_API_KEY.equals(apiKey)) {
            return true; // 验证通过
        }

        // 验证失败，返回错误
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);

        JSONObject error = new JSONObject();
        error.put("code", 401);
        error.put("msg", "API Key验证失败");
        error.put("success", false);
        error.put("time", System.currentTimeMillis());

        PrintWriter writer = response.getWriter();
        writer.write(error.toString());
        writer.flush();

        return false; // 拦截请求
    }
}