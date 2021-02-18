package com.xxx.websocket.request;


import com.xxx.enums.*;
import lombok.Data;

import java.math.BigDecimal;

@Data
public class OrderDto {
    private String symbol;
    private OrderSide side;
    private PositionSide positionSide;
    private OrderType type;
    private BigDecimal quantity;
    private BigDecimal price;
    private WorkingType workingType;
    private Boolean reduceOnly;
    private BigDecimal stopPrice;
    private String newClientOrderId;
    private NewOrderRespType newOrderRespType;
    private Boolean closePosition;
    private TimeInForce timeInForce;
}
