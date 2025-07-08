package com.xiaobai.project_data.service;

import com.xiaobai.project_data.domain.ApiKey;
import com.baomidou.mybatisplus.extension.service.IService;

public interface ApiKeyService extends IService<ApiKey> {
    boolean validateApiKey(String apiKey);
    ApiKey createApiKey(String description, String createdBy);
    boolean revokeApiKey(String apiKey);
}