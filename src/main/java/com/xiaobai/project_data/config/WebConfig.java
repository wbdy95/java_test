package com.xiaobai.project_data.config;

import com.xiaobai.project_data.interceptor.ApiKeyInterceptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private ApiKeyInterceptor apiKeyInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 对/data路径下的所有接口进行验证，但排除管理接口
        registry.addInterceptor(apiKeyInterceptor)
                .addPathPatterns("/data/**")
                .excludePathPatterns("/admin/**"); // 管理接口可以单独保护
    }
}