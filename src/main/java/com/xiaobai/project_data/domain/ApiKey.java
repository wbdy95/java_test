package com.xiaobai.project_data.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import java.util.Date;

@TableName("api_keys")
@Data
public class ApiKey {
    @TableId(type = IdType.AUTO)
    private Long id;

    private String keyValue;
    private String description;
    private boolean enabled;
    private Date createTime;
    private Date expireTime;
    private String createdBy;
}