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
import java.text.ParseException;
import java.time.Instant;
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
    public String getAllDataByDate(String startDate, String endDate, String adcenter, String country, int pageNum, int pageSize) {
        LambdaQueryWrapper<Data2025> queryWrapper = new LambdaQueryWrapper<>();

        try {
            if(endDate == null || endDate.isEmpty() ||startDate == null || startDate.isEmpty()){
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

                    ObjectMapper objectMapper = new ObjectMapper();
                    return objectMapper.writeValueAsString(data);
                }
            }
        } catch (JsonProcessingException ex) {
            throw new RuntimeException(ex);
        }
        try {
            if (startDate != null && !startDate.isEmpty()&&endDate != null && !endDate.isEmpty()) {
                LocalDate localStartDate = DateUtils.parseLocalDate(startDate);
                LocalDate localEndDate = DateUtils.parseLocalDate(endDate);
                // 查询当天所有数据
                queryWrapper.between(
                        Data2025::getDetatime,
                        java.sql.Date.valueOf(localStartDate),
                        java.sql.Date.valueOf(localEndDate.plusDays(1))
                );
            }

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

                ObjectMapper objectMapper = new ObjectMapper();

                return objectMapper.writeValueAsString(data);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "查询失败！";
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

                    ObjectMapper objectMapper = new ObjectMapper();

                    return objectMapper.writeValueAsString(data);
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

                ObjectMapper objectMapper = new ObjectMapper();

                return objectMapper.writeValueAsString(data);
            }
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
        return "查询失败！";
    }

}
