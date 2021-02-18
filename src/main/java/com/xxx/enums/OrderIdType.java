package com.xxx.enums;


public enum OrderIdType {
    B,S;

    public static boolean isCustomerId(String orderId){
        for(OrderIdType t:OrderIdType.values()){
            if(orderId.startsWith(t.toString())){
                return true;
            }
        }
        return false;
    }
}
