package com.xxx.dto;

import com.xxx.response.ApiResponse;
import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class Cancel extends ApiResponse {

    private Long orderId;
    private String symbol;
    private String status;
    private String clientOrderId;
    private String price;
    private String avgPrice;
    private String origQty;
    private String executedQty;
    private String cumQty;
    private String cumQuote;
    private String timeInForce;
    private String type;
    private Boolean reduceOnly;
    private Boolean closePosition;
    private String side;
    private String positionSide;
    private String stopPrice;
    private String workingType;
    private Boolean priceProtect;
    private String origType;
    private Long updateTime;
}
