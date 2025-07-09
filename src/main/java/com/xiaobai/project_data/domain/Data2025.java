package com.xiaobai.project_data.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Data;
import javax.validation.constraints.NotBlank;

@TableName(value ="data_2024")
@Data
public class Data2025 {

    @TableId(type = IdType.AUTO)
    private int id;

    @NotBlank(message = "gaid不能为空")
    private String gaid;

    private String model;

    @NotBlank(message = "country不能为空")
    private String country;

    @TableField(value = "ua")
    private String ua;

    @TableField(value = "adtype")
    private String adtype;

    @TableField(value = "adcenter")
    private String adcenter;

    @TableField(value = "packagename")
    private String packagename;

    @TableField(value = "currencycode")
    private String currencycode;

    @TableField(value = "valuemicros")
    private String valuemicros;

    @TableField(value = "precisiontype")
    private String precisiontype;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private Date detatime;
}