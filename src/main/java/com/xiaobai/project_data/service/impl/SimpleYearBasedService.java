package com.xiaobai.project_data.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.xiaobai.project_data.domain.Data2025;
import com.xiaobai.project_data.mapper.Data2025Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SimpleYearBasedService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据年份查询数据 - 使用JdbcTemplate原生SQL
     */
    public List<Data2025> queryDataByYear(int year, String startDate, String endDate,
                                          String adcenter, String country, String packagename,
                                          int pageNum, int pageSize) {

        String tableName = "data_" + year;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT * FROM ").append(tableName).append(" WHERE 1=1 ");

        // 动态添加条件
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND detatime >= '").append(startDate).append("' ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND detatime <= '").append(endDate).append("' ");
        }
        if (adcenter != null && !adcenter.isEmpty()) {
            sql.append(" AND adcenter = '").append(adcenter).append("' ");
        }
        if (country != null && !country.isEmpty()) {
            sql.append(" AND country = '").append(country).append("' ");
        }
        if (packagename != null && !packagename.isEmpty()) {
            sql.append(" AND packagename = '").append(packagename).append("' ");
        }

        sql.append(" ORDER BY valuemicros DESC ");

        // 添加分页
        if (pageNum > 0 && pageSize > 0) {
            int offset = (pageNum - 1) * pageSize;
            sql.append(" LIMIT ").append(offset).append(", ").append(pageSize);
        }

        return jdbcTemplate.query(sql.toString(), (rs, rowNum) -> {
            Data2025 data = new Data2025();
            data.setId(rs.getInt("id"));
            data.setGaid(rs.getString("gaid"));
            data.setModel(rs.getString("model"));
            data.setCountry(rs.getString("country"));
            data.setUa(rs.getString("ua"));
            data.setAdtype(rs.getString("adtype"));
            data.setAdcenter(rs.getString("adcenter"));
            data.setPackagename(rs.getString("packagename"));
            data.setCurrencycode(rs.getString("currencycode"));
            data.setValuemicros(rs.getString("valuemicros"));
            data.setPrecisiontype(rs.getString("precisiontype"));
            data.setDetatime(rs.getTimestamp("detatime"));
            return data;
        });
    }

    /**
     * 根据年份插入数据
     */
    public boolean insertDataByYear(Data2025 data, int year) {
        String tableName = "data_" + year;

        String sql = String.format(
                "INSERT INTO %s (gaid, model, country, ua, adtype, adcenter, packagename, currencycode, valuemicros, precisiontype, detatime) " +
                        "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)",
                tableName
        );

        try {
            int result = jdbcTemplate.update(sql,
                    data.getGaid(),
                    data.getModel(),
                    data.getCountry(),
                    data.getUa(),
                    data.getAdtype(),
                    data.getAdcenter(),
                    data.getPackagename(),
                    data.getCurrencycode(),
                    data.getValuemicros(),
                    data.getPrecisiontype(),
                    data.getDetatime()
            );
            return result > 0;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 根据年份获取数据总数
     */
    public int countDataByYear(int year, String startDate, String endDate,
                               String adcenter, String country, String packagename) {
        String tableName = "data_" + year;

        StringBuilder sql = new StringBuilder();
        sql.append("SELECT COUNT(*) FROM ").append(tableName).append(" WHERE 1=1 ");

        // 动态添加条件
        if (startDate != null && !startDate.isEmpty()) {
            sql.append(" AND detatime >= '").append(startDate).append("' ");
        }
        if (endDate != null && !endDate.isEmpty()) {
            sql.append(" AND detatime <= '").append(endDate).append("' ");
        }
        if (adcenter != null && !adcenter.isEmpty()) {
            sql.append(" AND adcenter = '").append(adcenter).append("' ");
        }
        if (country != null && !country.isEmpty()) {
            sql.append(" AND country = '").append(country).append("' ");
        }
        if (packagename != null && !packagename.isEmpty()) {
            sql.append(" AND packagename = '").append(packagename).append("' ");
        }

        return jdbcTemplate.queryForObject(sql.toString(), Integer.class);
    }

    /**
     * 从日期字符串中解析年份
     */
    public int parseYearFromDateString(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            return LocalDate.now().getYear();
        }
        try {
            return Integer.parseInt(dateStr.substring(0, 4));
        } catch (Exception e) {
            return LocalDate.now().getYear();
        }
    }
}