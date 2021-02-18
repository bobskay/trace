package com.xxx.utils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.serializer.SerializerFeature;

public class JsonUtil {
    public static String toJs(Object object) {
        return JSON.toJSONString(object);
    }

    public static String toFormatJs(Object object) {
        return JSON.toJSONString(object, SerializerFeature.PrettyFormat);
    }

    public static  <T> T toBean(String json, Class<T> clazz) {
        JSONObject obj = (JSONObject) JSON.parse(json);
        return obj.toJavaObject(clazz);
    }
}
