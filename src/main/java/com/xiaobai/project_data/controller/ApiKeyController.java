package com.xiaobai.project_data.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.project_data.domain.ApiKey;
import com.xiaobai.project_data.service.ApiKeyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin/api-keys")
public class ApiKeyController {

    @Autowired
    private ApiKeyService apiKeyService;

    /**
     * 创建新的API Key
     */
    @PostMapping("/create")
    public String createApiKey(@RequestParam String description,
                               @RequestParam(defaultValue = "admin") String createdBy) {
        try {
            ApiKey apiKey = apiKeyService.createApiKey(description, createdBy);

            JSONObject result = new JSONObject();
            result.put("code", 200);
            result.put("msg", "API Key创建成功");
            result.put("success", true);
            result.put("data", apiKey);
            result.put("time", System.currentTimeMillis());

            return result.toString();
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put("code", 500);
            result.put("msg", "API Key创建失败: " + e.getMessage());
            result.put("success", false);
            result.put("time", System.currentTimeMillis());

            return result.toString();
        }
    }

    /**
     * 撤销API Key
     */
    @PostMapping("/revoke")
    public String revokeApiKey(@RequestParam String apiKey) {
        try {
            boolean success = apiKeyService.revokeApiKey(apiKey);

            JSONObject result = new JSONObject();
            result.put("code", success ? 200 : 400);
            result.put("msg", success ? "API Key撤销成功" : "API Key撤销失败");
            result.put("success", success);
            result.put("time", System.currentTimeMillis());

            return result.toString();
        } catch (Exception e) {
            JSONObject result = new JSONObject();
            result.put("code", 500);
            result.put("msg", "API Key撤销失败: " + e.getMessage());
            result.put("success", false);
            result.put("time", System.currentTimeMillis());

            return result.toString();
        }
    }

    /**
     * 验证API Key
     */
    @GetMapping("/validate")
    public String validateApiKey(@RequestParam String apiKey) {
        boolean valid = apiKeyService.validateApiKey(apiKey);

        JSONObject result = new JSONObject();
        result.put("code", valid ? 200 : 400);
        result.put("msg", valid ? "API Key有效" : "API Key无效");
        result.put("valid", valid);
        result.put("success", valid);
        result.put("time", System.currentTimeMillis());

        return result.toString();
    }
}