package com.xxx.dto;

import com.alibaba.fastjson.JSONArray;
import com.xxx.response.ApiResponse;
import com.xxx.response.ListResponse;
import com.xxx.utils.JsonUtil;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
public class OpenOrders extends ApiResponse implements ListResponse {
    private List<OpenOrder> openOrders;

    @Override
    public void addResult(String response) {
        JSONArray array = JSONArray.parseArray(response);
        openOrders = new ArrayList<>();
        for (Object o : array) {
            OpenOrder openOrder = JsonUtil.toBean(o.toString(), OpenOrder.class);
            openOrders.add(openOrder);
        }
    }
}
