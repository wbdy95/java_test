package com.xiaobai.project_data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.xiaobai.project_data.domain.Data2025;
import com.xiaobai.project_data.domain.ReturnData;
import com.xiaobai.project_data.service.Data2025Service;
import com.xiaobai.project_data.mapper.Data2025Mapper;
import com.xiaobai.project_data.utils.DateUtils;
import com.alibaba.fastjson.JSONObject;
import java.time.LocalDate;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.stereotype.Service;

import javax.xml.crypto.Data;

/**
 * @author Administrator
 * @description 针对表【data_2025】的数据库操作Service实现
 * @createDate 2025-07-02 21:11:16
 */
@Service
public class Data2025ServiceImpl extends ServiceImpl<Data2025Mapper, Data2025>
        implements Data2025Service{

    @Override
    public String getAllDataByDate(String startDate, String endDate, String adcenter, String country, String packagename, int pageNum, int pageSize) {
        LambdaQueryWrapper<Data2025> queryWrapper = new LambdaQueryWrapper<>();

        try {
            // 处理日期范围
            if (endDate == null || endDate.isEmpty() || startDate == null || startDate.isEmpty()) {
                // 如果没有提供日期范围，查询当天数据
                Date nowDate = new Date();
                String format = DateUtils.format(nowDate);
                LocalDate localDate = DateUtils.parseLocalDate(format);
                queryWrapper.between(
                        Data2025::getDetatime,
                        java.sql.Date.valueOf(localDate),
                        java.sql.Date.valueOf(localDate.plusDays(1))
                );
            } else {
                // 查询指定时间段数据
                LocalDate localStartDate = DateUtils.parseLocalDate(startDate);
                LocalDate localEndDate = DateUtils.parseLocalDate(endDate);
                queryWrapper.between(
                        Data2025::getDetatime,
                        java.sql.Date.valueOf(localStartDate),
                        java.sql.Date.valueOf(localEndDate.plusDays(1))
                );
            }

            // 添加条件查询
            if (adcenter != null && !adcenter.isEmpty()) {
                queryWrapper.eq(Data2025::getAdcenter, adcenter);
            }
            if (country != null && !country.isEmpty()) {
                queryWrapper.eq(Data2025::getCountry, country);
            }
            if (packagename != null && !packagename.isEmpty()) {
                queryWrapper.eq(Data2025::getPackagename, packagename);
            }

            // 按照 valuemicros 从大到小排序
            queryWrapper.orderByDesc(Data2025::getValuemicros);

            // 分页查询
            if (pageNum > 0 && pageSize > 0) {
                Page<Data2025> page = new Page<>(pageNum, pageSize);
                Page<Data2025> pageResult = this.page(page, queryWrapper);
                List<Data2025> records = pageResult.getRecords();

                // 获取总记录数
                int total = Math.toIntExact(this.count(queryWrapper));
                // 计算总页数
                int totalPage = (total + pageSize - 1) / pageSize;

                ReturnData data = new ReturnData();
                data.setData(records);
                data.setTotal(total);
                data.setTotalPage(totalPage);

                JSONObject object = new JSONObject();
                object.put("info", data);
                object.put("code", 200);
                object.put("msg", "OK");
                object.put("success", true);
                object.put("time", System.currentTimeMillis());
                return object.toString();
            } else {
                // 如果没有分页参数，返回所有数据
                List<Data2025> allRecords = this.list(queryWrapper);
                ReturnData data = new ReturnData();
                data.setData(allRecords);
                data.setTotal(allRecords.size());
                data.setTotalPage(1);

                JSONObject object = new JSONObject();
                object.put("info", data);
                object.put("code", 200);
                object.put("msg", "OK");
                object.put("success", true);
                object.put("time", System.currentTimeMillis());
                return object.toString();
            }

        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("msg", "查询失败：" + e.getMessage());
            object.put("success", false);
            object.put("code", 400);
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
    }


    @Override
    public String getDataByDate(String startDate, String adcenter, String country, int pageNum, int pageSize) {
        LambdaQueryWrapper<Data2025> queryWrapper = new LambdaQueryWrapper<>();

        try {
            if(startDate==null||startDate.isEmpty()){
                Date nowDate = new Date();
                String format = DateUtils.format(nowDate);
                LocalDate localDate = DateUtils.parseLocalDate(format);
                // 查询当天所有数据
                queryWrapper.between(
                        Data2025::getDetatime,
                        java.sql.Date.valueOf(localDate),
                        java.sql.Date.valueOf(localDate.plusDays(1))
                );

                if(adcenter!=null&&!adcenter.isEmpty()){
                    queryWrapper.eq(Data2025::getAdcenter,adcenter);
                }
                if(country!=null&&!country.isEmpty()){
                    queryWrapper.eq(Data2025::getCountry,country);
                }
                queryWrapper.orderByDesc(Data2025::getValuemicros);

                if (pageNum != 0 && pageSize != 0) {
                    Page<Data2025> page = new Page<>(pageNum, pageSize);
                    Page<Data2025> page1 = this.page(page, queryWrapper);
                    List<Data2025> records = page1.getRecords();

                    // 获取总记录数
                    int total = Math.toIntExact(this.count(queryWrapper));
                    // 计算总页数
                    int totalPage = (total + pageSize - 1) / pageSize;

                    ReturnData data = new ReturnData();
                    data.setData(records);
                    data.setTotal(total);
                    data.setTotalPage(totalPage);

                    JSONObject object = new JSONObject();
                    object.put("info",data);
                    object.put("code", Integer.parseInt("200"));
                    object.put("msg", "OK");
                    object.put("success", true);
                    object.put("time", System.currentTimeMillis());
                    return object.toString();
                }
            }
            LocalDate localDate = DateUtils.parseLocalDate(startDate);
            // 查询当天所有数据
            queryWrapper.between(
                    Data2025::getDetatime,
                    java.sql.Date.valueOf(localDate),
                    java.sql.Date.valueOf(localDate.plusDays(1))
            );
            if(adcenter!=null&&!adcenter.isEmpty()){
                queryWrapper.eq(Data2025::getAdcenter,adcenter);
            }
            if(country!=null&&!country.isEmpty()){
                queryWrapper.eq(Data2025::getCountry,country);
            }
            queryWrapper.orderByDesc(Data2025::getValuemicros);
            if (pageNum != 0 && pageSize != 0) {
                Page<Data2025> page = new Page<>(pageNum, pageSize);
                Page<Data2025> page1 = this.page(page, queryWrapper);
                List<Data2025> records = page1.getRecords();

                // 获取总记录数
                int total = Math.toIntExact(this.count(queryWrapper));
                // 计算总页数
                int totalPage = (total + pageSize - 1) / pageSize;

                ReturnData data = new ReturnData();
                data.setData(records);
                data.setTotal(total);
                data.setTotalPage(totalPage);

                JSONObject object = new JSONObject();
                object.put("info",data);
                object.put("code", Integer.parseInt("200"));
                object.put("msg", "OK");
                object.put("success", true);
                object.put("time", System.currentTimeMillis());
                return object.toString();
            }
        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("msg", "查询失败！");
            object.put("success", false);
            object.put("code", Integer.parseInt("400"));
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
        JSONObject object = new JSONObject();
        object.put("msg", "查询失败！");
        object.put("success", false);
        object.put("code", Integer.parseInt("400"));
        object.put("time", System.currentTimeMillis());
        return object.toString();
    }

}