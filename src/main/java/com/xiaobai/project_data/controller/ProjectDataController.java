package com.xiaobai.project_data.controller;

import com.xiaobai.project_data.domain.Data2025;
import com.xiaobai.project_data.service.Data2025Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Resource;
import javax.validation.Valid;
import java.util.Date;

@RestController
@RequestMapping("/data")
public class ProjectDataController {

    @Resource
    private Data2025Service data2025Service;

    /**
     * 添加数据 - 现在需要API Key验证
     */
    @PostMapping("/insert")
    public String insert(@Valid @RequestBody Data2025 data2025) {
        try {
            data2025.setDetatime(new Date());
            boolean save = data2025Service.save(data2025);

            if (save) {
                JSONObject object = new JSONObject();
                object.put("msg", "添加成功！");
                object.put("success", true);
                object.put("code", 200);
                object.put("time", System.currentTimeMillis());
                return object.toString();
            }

            JSONObject object = new JSONObject();
            object.put("msg", "添加失败！");
            object.put("success", false);
            object.put("code", 400);
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
     * 查询指定时间段数据 - 现在需要API Key验证
     */
    @GetMapping("/getAllDataByDate")
    public String getAllDataByDate(@RequestParam(value = "startDate",required = false) String startDate,
                                   @RequestParam(value = "endDate",required = false) String endDate,
                                   @RequestParam(value = "adcenter",required = false) String adcenter,
                                   @RequestParam(value = "country",required = false) String country,
                                   @RequestParam(value = "packagename",required = false) String packagename,
                                   @RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,
                                   @RequestParam(value = "pageSize",required = false,defaultValue = "100") int pageSize) {
        return data2025Service.getAllDataByDate(startDate,endDate,adcenter,country,packagename,pageNum,pageSize);
    }

    /**
     * 查询指定时间数据 - 现在需要API Key验证
     */
    @GetMapping("/getDataByDate")
    public String getDataByDate(@RequestParam(value = "startDate",required = false) String startDate,
                                @RequestParam(value = "adcenter",required = false) String adcenter,
                                @RequestParam(value = "country",required = false) String country,
                                @RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,
                                @RequestParam(value = "pageSize",required = false,defaultValue = "100") int pageSize) {
        return data2025Service.getDataByDate(startDate,adcenter,country,pageNum,pageSize);
    }
}