package com.xxx;

import com.alibaba.fastjson.JSONObject;
import com.xxx.websocket.listener.AccountListener;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class OrderTradeUpdateListener implements AccountListener {
    private Market market;

    public OrderTradeUpdateListener(Market market) {
        this.market = market;
    }

    @Override
    public boolean listen(JSONObject js) {
        JSONObject order = js.getJSONObject("o");
        market.orderChange(order);
        return true;

    }

    public String eventName() {
        return "ORDER_TRADE_UPDATE";
    }

}
