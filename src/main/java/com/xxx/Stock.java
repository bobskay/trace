package com.xxx;

import com.xxx.dto.Account;
import com.xxx.dto.OpenOrder;
import com.xxx.enums.OrderIdType;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

//账户信息
@Data
@Slf4j
public class Stock {
    private int holds;
    private transient ConcurrentHashMap<String, OrderVo> buys;
    private transient ConcurrentHashMap<String,OrderVo> sells;

    public int sells() {
        return getAmount(sells);
    }
    public int buys() {
        return getAmount(buys);
    }

    public int getAmount(Map<String,OrderVo> orderVos){
        int amount=0;
        for(OrderVo o:orderVos.values()){
            amount+=o.quantity;
        }
        return amount;
    }

    public String toString(){
        StringBuilder sb=new StringBuilder();
        sb.append("hold=").append(holds);
        sb.append(",buys=").append(getAmount(buys));
        sb.append(",sells=").append(getAmount(sells));
        return sb.toString();
    }

    public List<OrderVo> sellList(){
        List<OrderVo>list=new ArrayList(sells.values());
        Collections.sort(list,(Comparator.comparingInt(o -> o.price)));
        return list;
    }

    public List<OrderVo> buyList(){
        List<OrderVo>list=new ArrayList(buys.values());
        Collections.sort(list,(Comparator.comparingInt(o -> o.price)));
        return list;
    }


    public static Stock createStock(Exchange exchange) {
        Stock stock = new Stock();
        stock.setBuys(new ConcurrentHashMap<>());
        stock.setSells(new ConcurrentHashMap<>());

        List<OpenOrder> ors = exchange.openOrders();
        for (OpenOrder openOd : ors) {
            if(!OrderIdType.isCustomerId(openOd.getClientOrderId())){
                log.info("跳过订单："+openOd.getClientOrderId());
                continue;
            }
            OrderVo orderVo = OrderVo.newOrder(openOd);
            if ("SELL".equalsIgnoreCase(openOd.getSide())) {
                stock.getSells().put(orderVo.orderId, orderVo);
            } else {
                stock.getBuys().put(orderVo.orderId, orderVo);
            }
        }

        Account account = exchange.account();
        for (Account.PositionsDTO positionsDTO : account.getPositions()) {
            if (positionsDTO.getSymbol().equalsIgnoreCase(exchange.getSymbol())) {
                //持有
                int hold = positionsDTO.getPositionAmt().intValue();
                stock.setHolds(hold);
                break;
            }
        }
        return stock;
    }
}
