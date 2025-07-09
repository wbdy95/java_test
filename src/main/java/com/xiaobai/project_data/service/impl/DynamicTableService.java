package com.xiaobai.project_data.service.impl;

import com.xiaobai.project_data.domain.Data2025;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class DynamicTableService {

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 根据年份和季度生成表名
     * @param year 年份
     * @param quarter 季度 (1-4)
     * @return 表名，例如：data_2025_q1
     */
    public String generateTableName(int year, int quarter) {
        return String.format("data_%d_q%d", year, quarter);
    }

    /**
     * 根据日期获取季度
     * @param date 日期
     * @return 季度 (1-4)
     */
    public int getQuarterFromDate(LocalDate date) {
        int month = date.getMonthValue();
        return (month - 1) / 3 + 1;
    }

    /**
     * 从日期字符串解析年份和季度
     * @param dateStr 日期字符串 "yyyy-MM-dd"
     * @return int[]{year, quarter}
     */
    public int[] parseYearAndQuarter(String dateStr) {
        if (dateStr == null || dateStr.isEmpty()) {
            LocalDate now = LocalDate.now();
            return new int[]{now.getYear(), getQuarterFromDate(now)};
        }
        try {
            LocalDate date = LocalDate.parse(dateStr);
            return new int[]{date.getYear(), getQuarterFromDate(date)};
        } catch (Exception e) {
            LocalDate now = LocalDate.now();
            return new int[]{now.getYear(), getQuarterFromDate(now)};
        }
    }

    /**
     * 插入数据到指定年份季度表
     */
    public boolean insertDataByYearQuarter(Data2025 data, int year, int quarter) {
        String tableName = generateTableName(year, quarter);

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
     * 根据日期自动插入数据（自动确定年份和季度）
     */
    public boolean insertDataByDate(Data2025 data, String dateStr) {
        int[] yearQuarter = parseYearAndQuarter(dateStr);
        return insertDataByYearQuarter(data, yearQuarter[0], yearQuarter[1]);
    }

    /**
     * 查询指定年份季度的数据
     */
    public List<Data2025> queryDataByYearQuarter(int year, int quarter, String startDate, String endDate,
                                                 String adcenter, String country, String packagename,
                                                 int pageNum, int pageSize) {
        String tableName = generateTableName(year, quarter);

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
     * 根据日期范围查询数据（可能跨多个季度）
     */
    public List<Data2025> queryDataByDateRange(String startDate, String endDate,
                                               String adcenter, String country, String packagename,
                                               int pageNum, int pageSize) {

        int[] startYearQuarter = parseYearAndQuarter(startDate);
        int[] endYearQuarter = parseYearAndQuarter(endDate);

        // 如果在同一个季度，直接查询
        if (startYearQuarter[0] == endYearQuarter[0] && startYearQuarter[1] == endYearQuarter[1]) {
            return queryDataByYearQuarter(startYearQuarter[0], startYearQuarter[1],
                    startDate, endDate, adcenter, country, packagename, pageNum, pageSize);
        }

        // 跨季度查询需要特殊处理
        // 这里简化处理，只查询开始日期所在季度的数据
        // 实际应用中可能需要更复杂的逻辑来合并多个季度的数据
        return queryDataByYearQuarter(startYearQuarter[0], startYearQuarter[1],
                startDate, endDate, adcenter, country, packagename, pageNum, pageSize);
    }

    /**
     * 获取指定年份季度的数据总数
     */
    public int countDataByYearQuarter(int year, int quarter, String startDate, String endDate,
                                      String adcenter, String country, String packagename) {
        String tableName = generateTableName(year, quarter);

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
     * 检查指定年份季度的表是否存在
     */
    public boolean checkTableExists(int year, int quarter) {
        String tableName = generateTableName(year, quarter);
        String sql = "SELECT COUNT(*) FROM information_schema.tables WHERE table_name = ? AND table_schema = DATABASE()";

        try {
            Integer count = jdbcTemplate.queryForObject(sql, Integer.class, tableName);
            return count != null && count > 0;
        } catch (Exception e) {
            return false;
        }
    }

    /**
     * 创建指定年份季度的表（基于已有表结构）
     */
    public boolean createTableIfNotExists(int year, int quarter) {
        String tableName = generateTableName(year, quarter);

        if (checkTableExists(year, quarter)) {
            return true;
        }

        String sql = String.format(
                "CREATE TABLE %s LIKE data_2025", tableName
        );

        try {
            jdbcTemplate.execute(sql);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
}