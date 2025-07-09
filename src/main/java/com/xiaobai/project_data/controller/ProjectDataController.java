package com.xiaobai.project_data.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.project_data.domain.Data2025;
import com.xiaobai.project_data.service.impl.DynamicTableService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.util.StringUtils;
import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@RestController
@RequestMapping("/data")
public class ProjectDataController {

    @Autowired
    private DynamicTableService dynamicTableService;

    /**
     * 添加数据 - 自动根据当前日期确定年份和季度
     */
    @PostMapping("/insert")
    public String insert(@RequestBody Data2025 data2025) {
        try {
            data2025.setDetatime(new Date());
            String gaid = data2025.getGaid();
            if (StringUtils.isEmpty(gaid)) {
                JSONObject object = new JSONObject();
                object.put("msg", "gaid null");
                object.put("success", false);
                object.put("code", 400);
                object.put("time", System.currentTimeMillis());
                return object.toString();
            }

            // 根据当前日期自动确定表
            String dateStr = LocalDate.now().toString();
            boolean save = dynamicTableService.insertDataByDate(data2025, dateStr);

            JSONObject object = new JSONObject();
            if (save) {
                object.put("msg", "添加成功！");
                object.put("success", true);
                object.put("code", 200);
            } else {
                object.put("msg", "添加失败！");
                object.put("success", false);
                object.put("code", 400);
            }
            object.put("time", System.currentTimeMillis());
            return object.toString();

        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("msg", "添加失败!");
            object.put("success", false);
            object.put("code", 400);
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
    }

    /**
     * 根据日期范围查询数据（自动确定季度表）
     */
    @GetMapping("/getAllDataByDate")
    public String getAllDataByDate(@RequestParam(value = "startDate", required = false) String startDate,
                                   @RequestParam(value = "endDate", required = false) String endDate,
                                   @RequestParam(value = "adcenter", required = false) String adcenter,
                                   @RequestParam(value = "country", required = false) String country,
                                   @RequestParam(value = "packagename", required = false) String packagename,
                                   @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize", required = false, defaultValue = "100") int pageSize) {
        try {
            // 根据日期范围查询数据
            List<Data2025> data = dynamicTableService.queryDataByDateRange(
                    startDate, endDate, adcenter, country, packagename, pageNum, pageSize);

            // 获取总数（这里简化处理，实际可能需要更复杂的逻辑）
            int[] yearQuarter = dynamicTableService.parseYearAndQuarter(startDate);
            int total = dynamicTableService.countDataByYearQuarter(
                    yearQuarter[0], yearQuarter[1], startDate, endDate, adcenter, country, packagename);

            int totalPage = (total + pageSize - 1) / pageSize;

            JSONObject result = new JSONObject();
            JSONObject info = new JSONObject();
            info.put("data", data);
            info.put("total", total);
            info.put("totalPage", totalPage);

            result.put("info", info);
            result.put("code", 200);
            result.put("msg", "OK");
            result.put("success", true);
            result.put("time", System.currentTimeMillis());

            return result.toString();

        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("msg", "查询失败：" + e.getMessage());
            object.put("success", false);
            object.put("code", 400);
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
    }

    /**
     * 指定年份和季度查询数据
     */
    @GetMapping("/getDataByYearQuarter")
    public String getDataByYearQuarter(@RequestParam(value = "year") int year,
                                       @RequestParam(value = "quarter") int quarter,
                                       @RequestParam(value = "startDate", required = false) String startDate,
                                       @RequestParam(value = "endDate", required = false) String endDate,
                                       @RequestParam(value = "adcenter", required = false) String adcenter,
                                       @RequestParam(value = "country", required = false) String country,
                                       @RequestParam(value = "packagename", required = false) String packagename,
                                       @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                       @RequestParam(value = "pageSize", required = false, defaultValue = "100") int pageSize) {
        try {
            // 验证季度参数
            if (quarter < 1 || quarter > 4) {
                JSONObject object = new JSONObject();
                object.put("msg", "季度参数必须在1-4之间");
                object.put("success", false);
                object.put("code", 400);
                object.put("time", System.currentTimeMillis());
                return object.toString();
            }

            // 检查表是否存在，如果不存在则创建
            if (!dynamicTableService.checkTableExists(year, quarter)) {
                boolean created = dynamicTableService.createTableIfNotExists(year, quarter);
                if (!created) {
                    JSONObject object = new JSONObject();
                    object.put("msg", "目标表不存在且创建失败");
                    object.put("success", false);
                    object.put("code", 400);
                    object.put("time", System.currentTimeMillis());
                    return object.toString();
                }
            }

            List<Data2025> data = dynamicTableService.queryDataByYearQuarter(
                    year, quarter, startDate, endDate, adcenter, country, packagename, pageNum, pageSize);

            int total = dynamicTableService.countDataByYearQuarter(
                    year, quarter, startDate, endDate, adcenter, country, packagename);

            int totalPage = (total + pageSize - 1) / pageSize;

            JSONObject result = new JSONObject();
            JSONObject info = new JSONObject();
            info.put("data", data);
            info.put("total", total);
            info.put("totalPage", totalPage);

            result.put("info", info);
            result.put("code", 200);
            result.put("msg", "OK");
            result.put("success", true);
            result.put("time", System.currentTimeMillis());

            return result.toString();

        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("msg", "查询失败：" + e.getMessage());
            object.put("success", false);
            object.put("code", 400);
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
    }

    /**
     * 创建指定年份季度的表
     */
    @PostMapping("/createTable")
    public String createTable(@RequestParam(value = "year") int year,
                              @RequestParam(value = "quarter") int quarter) {
        try {
            if (quarter < 1 || quarter > 4) {
                JSONObject object = new JSONObject();
                object.put("msg", "季度参数必须在1-4之间");
                object.put("success", false);
                object.put("code", 400);
                object.put("time", System.currentTimeMillis());
                return object.toString();
            }

            boolean created = dynamicTableService.createTableIfNotExists(year, quarter);
            String tableName = dynamicTableService.generateTableName(year, quarter);

            JSONObject object = new JSONObject();
            if (created) {
                object.put("msg", "表 " + tableName + " 创建成功或已存在");
                object.put("success", true);
                object.put("code", 200);
            } else {
                object.put("msg", "表 " + tableName + " 创建失败");
                object.put("success", false);
                object.put("code", 400);
            }
            object.put("time", System.currentTimeMillis());
            return object.toString();

        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("msg", "创建表失败：" + e.getMessage());
            object.put("success", false);
            object.put("code", 400);
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
    }

    /**
     * 检查指定年份季度的表是否存在
     */
    @GetMapping("/checkTable")
    public String checkTable(@RequestParam(value = "year") int year,
                             @RequestParam(value = "quarter") int quarter) {
        try {
            if (quarter < 1 || quarter > 4) {
                JSONObject object = new JSONObject();
                object.put("msg", "季度参数必须在1-4之间");
                object.put("success", false);
                object.put("code", 400);
                object.put("time", System.currentTimeMillis());
                return object.toString();
            }

            boolean exists = dynamicTableService.checkTableExists(year, quarter);
            String tableName = dynamicTableService.generateTableName(year, quarter);

            JSONObject object = new JSONObject();
            object.put("tableName", tableName);
            object.put("exists", exists);
            object.put("msg", exists ? "表存在" : "表不存在");
            object.put("success", true);
            object.put("code", 200);
            object.put("time", System.currentTimeMillis());
            return object.toString();

        } catch (Exception e) {
            JSONObject object = new JSONObject();
            object.put("msg", "检查表失败：" + e.getMessage());
            object.put("success", false);
            object.put("code", 400);
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
    }
}