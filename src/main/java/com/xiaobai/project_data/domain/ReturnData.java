package com.xiaobai.project_data.domain;

import lombok.Data;

import java.util.List;

@Data
public class ReturnData {
    private List<Data2025> data;

    private int totalPage;

    private int total;
}
