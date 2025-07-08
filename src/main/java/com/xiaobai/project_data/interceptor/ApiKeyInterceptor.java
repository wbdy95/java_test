package com.xiaobai.project_data.interceptor;

import com.alibaba.fastjson.JSONObject;

import com.xiaobai.project_data.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Component
public class ApiKeyInterceptor implements HandlerInterceptor {

    @Autowired
    private ApiKeyService apiKeyService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {

        // 获取请求头中的API Key
        String apiKey = request.getHeader("api-key");
        if (apiKey == null || apiKey.trim().isEmpty()) {
            apiKey = request.getParameter("api-key"); // 也支持参数方式
        }

        // 动态验证API Key
        if (apiKeyService.validateApiKey(apiKey)) {
            return true; // 验证通过
        }

        // 验证失败，返回错误
        response.setContentType("application/json;charset=UTF-8");
        response.setStatus(401);

        JSONObject error = new JSONObject();
        error.put("code", 401);
        error.put("msg", "API Key验证失败或已过期");
        error.put("success", false);
        error.put("time", System.currentTimeMillis());

        PrintWriter writer = response.getWriter();
        writer.write(error.toString());
        writer.flush();

        return false; // 拦截请求
    }
}