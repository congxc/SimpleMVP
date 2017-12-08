package com.rainshieh.simplemvp.model.http.exception;

/**
 * author: xiecong
 * create time: 2017/12/7 18:42
 * lastUpdate time: 2017/12/7 18:42
 */

public class NetException extends Exception{
    public NetException(String detailMessage) {
        super(detailMessage);
    }
}
