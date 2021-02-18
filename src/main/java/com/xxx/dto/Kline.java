package com.xxx.dto;

import com.xxx.response.ApiResponse;
import com.xxx.utils.DateTime;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Kline extends ApiResponse {
    private DateTime time;
    private BigDecimal open;
    private BigDecimal high;
    private BigDecimal low;
    private BigDecimal close;
}
