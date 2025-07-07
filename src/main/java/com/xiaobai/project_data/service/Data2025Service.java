package com.xiaobai.project_data.service;

import com.xiaobai.project_data.domain.Data2025;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author Administrator
* @description 针对表【data_2025】的数据库操作Service
* @createDate 2025-07-02 21:11:16
*/
public interface Data2025Service extends IService<Data2025> {

    String getAllDataByDate(String startDate, String endDate, String adcenter, String country, String packagename, int pageNum, int pageSize);

    String getDataByDate(String startDate, String adcenter, String country, int pageNum, int pageSize);
}
