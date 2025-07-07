package com.xiaobai.project_data.controller;

import com.xiaobai.project_data.domain.Data2025;
import com.xiaobai.project_data.service.Data2025Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;
import com.alibaba.fastjson.JSONObject;
import javax.annotation.Resource;
import java.nio.charset.StandardCharsets;
import java.util.Date;

@RestController
@RequestMapping("/data")
public class ProjectDataController {


    @Resource
    private Data2025Service data2025Service;


    /**
     * 添加数据
     * @param data2025  数据
     * @return 结果
     */
    @PostMapping("/insert")
    public String insert(@RequestBody Data2025 data2025) {
        String gaid = data2025.getGaid() ;
        if(StringUtils.isEmpty(gaid)){
            JSONObject object = new JSONObject();
            object.put("msg", "添加失败！gaid不能为空");
            object.put("success", false);
            object.put("code", Integer.parseInt("400"));
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
        else{
            boolean save = data2025Service.save(data2025);
            data2025.setDetatime(new Date());
            if(save){
                JSONObject object = new JSONObject();
                object.put("msg", "添加成功！");
                object.put("success", false);
                object.put("code", Integer.parseInt("200"));
                object.put("time", System.currentTimeMillis());
                return object.toString();
            }
            JSONObject object = new JSONObject();
            object.put("msg", "添加失败！");
            object.put("success", false);
            object.put("code", Integer.parseInt("400"));
            object.put("time", System.currentTimeMillis());
            return object.toString();
        }
    }


    /**
     * 查询指定时间段数据
     * @param startDate 开始时间
     * @param endDate 结束时间
     * @param adcenter 广告中心
     * @param country 国家
     * @param packagename 包名
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 结果
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
     * 查询指定时间数据
     * @param startDate 开始时间
     * @param adcenter 广告中心
     * @param country 国家
     * @param pageNum 页码
     * @param pageSize 每页大小
     * @return 结果
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
