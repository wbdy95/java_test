package com.xiaobai.project_data.controller;

import com.xiaobai.project_data.domain.Data2025;
import com.xiaobai.project_data.service.Data2025Service;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

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
            return "添加失败！gaid不为空";
        }
        else{
            boolean save = data2025Service.save(data2025);
            data2025.setDetatime(new Date());
            if(save){
                return "添加成功！";
            }
            return "添加失败！";
        }
    }


    /**
     * 查询指定时间段数据
     * @param startDate 开始时间
     *        endDate 结束时间
     *        adcenter 广告中心
     * @return 结果
     */
    @GetMapping("/getAllDataByDate")
    public String getAllDataByDate(@RequestParam(value = "startDate",required = false) String startDate,@RequestParam(value = "endDate",required = false) String endDate,
                                   @RequestParam(value = "adcenter",required = false) String adcenter,@RequestParam(value = "country",required = false) String country,
                                   @RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",required = false,defaultValue = "100") int pageSize) {
        return data2025Service.getAllDataByDate(startDate,endDate,adcenter,country,pageNum,pageSize);
    }

    /**
     * 查询指定时间数据
     * @param startDate 开始时间
     *        adcenter 广告中心
     * @return 结果
     */
    @GetMapping("/getDataByDate")
    public String getDataByDate(@RequestParam(value = "startDate",required = false) String startDate,
                                   @RequestParam(value = "adcenter",required = false) String adcenter,@RequestParam(value = "country",required = false) String country,
                                   @RequestParam(value = "pageNum",required = false,defaultValue = "1") int pageNum,@RequestParam(value = "pageSize",required = false,defaultValue = "100") int pageSize) {
        return data2025Service.getDataByDate(startDate,adcenter,country,pageNum,pageSize);
    }
}
