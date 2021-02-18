package com.xxx.websocket.listener;

import com.alibaba.fastjson.JSONObject;
import com.xxx.websocket.Stream;

public interface MessageListener extends AccountListener {
    boolean listen(JSONObject jsonObject);

    Stream stream();


}
