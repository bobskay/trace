package com.xxx;

import com.xxx.dto.OpenOrder;

import java.math.BigDecimal;

public class OrderVo {
    public int quantity;
    public int price;
    public String orderId;


    public static  OrderVo newOrder(OpenOrder openOrder) {
        OrderVo vo= newOrder(openOrder.getPrice(),openOrder.getOrigQty(),openOrder.getClientOrderId());
      return vo;
    }

    public static  OrderVo newOrder(String price, String origQty,String orderId) {
        OrderVo od=new OrderVo();
        od.price=new BigDecimal(price).intValue();
        od.quantity=new BigDecimal(origQty).intValue();
        od.orderId=orderId;
        return od;
    }

    public String toString(){
        return price+"*"+quantity;
    }

}
