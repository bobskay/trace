package com.xxx.websocket.listener;


import com.alibaba.fastjson.JSONObject;

public interface AccountListener {

    boolean listen(JSONObject jsonObject);

    String eventName();
}
