package com.xxx.exception;

public class BinanceApiException  extends RuntimeException{
    public BinanceApiException(String message,Exception e){
        super(message,e);
    }

    public BinanceApiException(Exception ex){
        super(ex);
    }

    public BinanceApiException(String message){
        super(message);
    }
}
