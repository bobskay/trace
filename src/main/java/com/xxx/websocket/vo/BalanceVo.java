package com.xxx.websocket.vo;

import com.xxx.utils.DateTime;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class BalanceVo {
    private BigDecimal total;
    private BigDecimal available;
    private DateTime updated;
    private String symbol;
}
