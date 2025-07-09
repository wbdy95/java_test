package com.xiaobai.project_data.controller;

import com.alibaba.fastjson.JSONObject;
import com.xiaobai.project_data.domain.Data2025;
import com.xiaobai.project_data.service.impl.SimpleYearBasedService;
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
    private SimpleYearBasedService yearBasedService;

    /**
     * 添加数据 - 自动根据当前年份选择表
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
            int currentYear = LocalDate.now().getYear();
//
            boolean save = yearBasedService.insertDataByYear(data2025, currentYear);

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
            object.put("msg", "添加失败：" + e.getMessage());
            object.put("success", false);
            object.put("code", 400);
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
    }

    /**
     * 查询数据 - 根据日期自动选择对应年份的表
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
            // 根据开始日期确定年份
            int year = yearBasedService.parseYearFromDateString(startDate);

            List<Data2025> data = yearBasedService.queryDataByYear(year, startDate, endDate,
                    adcenter, country, packagename,
                    pageNum, pageSize);

            int total = yearBasedService.countDataByYear(year, startDate, endDate,
                    adcenter, country, packagename);

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
     * 指定年份查询数据
     */
    @GetMapping("/getDataByYear")
    public String getDataByYear(@RequestParam(value = "year") int year,
                                @RequestParam(value = "startDate", required = false) String startDate,
                                @RequestParam(value = "endDate", required = false) String endDate,
                                @RequestParam(value = "adcenter", required = false) String adcenter,
                                @RequestParam(value = "country", required = false) String country,
                                @RequestParam(value = "packagename", required = false) String packagename,
                                @RequestParam(value = "pageNum", required = false, defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize", required = false, defaultValue = "100") int pageSize) {
        try {
            List<Data2025> data = yearBasedService.queryDataByYear(year, startDate, endDate,
                    adcenter, country, packagename,
                    pageNum, pageSize);

            int total = yearBasedService.countDataByYear(year, startDate, endDate,
                    adcenter, country, packagename);

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
}