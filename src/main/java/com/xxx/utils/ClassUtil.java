package com.xxx.utils;

import lombok.SneakyThrows;

public class ClassUtil {
    @SneakyThrows
    public static <T> T newInstance(Class<T> clz){
        return clz.newInstance();
    }
}
