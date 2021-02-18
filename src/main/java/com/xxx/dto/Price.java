package com.xxx.dto;

import com.xxx.response.ApiResponse;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class Price extends ApiResponse {
    private String symbol;
    private BigDecimal price;
    private Long time;
}
