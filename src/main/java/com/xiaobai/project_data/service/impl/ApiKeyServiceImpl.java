package com.xiaobai.project_data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xiaobai.project_data.domain.ApiKey;
import com.xiaobai.project_data.mapper.ApiKeyMapper;
import com.xiaobai.project_data.service.ApiKeyService;
import org.springframework.stereotype.Service;
import java.security.SecureRandom;
import java.util.Date;

@Service
public class ApiKeyServiceImpl extends ServiceImpl<ApiKeyMapper, ApiKey> implements ApiKeyService {

    @Override
    public boolean validateApiKey(String apiKey) {
        if (apiKey == null || apiKey.trim().isEmpty()) {
            return false;
        }

        LambdaQueryWrapper<ApiKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiKey::getKeyValue, apiKey)
                .eq(ApiKey::isEnabled, true);

        ApiKey apiKeyEntity = this.getOne(queryWrapper);

        if (apiKeyEntity == null) {
            return false;
        }

        // 检查是否过期
        if (apiKeyEntity.getExpireTime() != null &&
                apiKeyEntity.getExpireTime().before(new Date())) {
            return false;
        }

        return true;
    }

    @Override
    public ApiKey createApiKey(String description, String createdBy) {
        ApiKey apiKey = new ApiKey();
        apiKey.setKeyValue(generateApiKey());
        apiKey.setDescription(description);
        apiKey.setEnabled(true);
        apiKey.setCreateTime(new Date());
        apiKey.setCreatedBy(createdBy);

        this.save(apiKey);
        return apiKey;
    }

    @Override
    public boolean revokeApiKey(String apiKey) {
        LambdaQueryWrapper<ApiKey> queryWrapper = new LambdaQueryWrapper<>();
        queryWrapper.eq(ApiKey::getKeyValue, apiKey);

        ApiKey apiKeyEntity = this.getOne(queryWrapper);
        if (apiKeyEntity != null) {
            apiKeyEntity.setEnabled(false);
            return this.updateById(apiKeyEntity);
        }
        return false;
    }

    private String generateApiKey() {
        SecureRandom random = new SecureRandom();
        StringBuilder sb = new StringBuilder();
        String chars = "0123456789abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";

        for (int i = 0; i < 32; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }

        return sb.toString();
    }
}