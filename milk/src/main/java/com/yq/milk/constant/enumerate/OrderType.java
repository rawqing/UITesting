package com.yq.milk.constant.enumerate;

/**
 * Created by king on 17/5/26.
 */

public enum OrderType {
    CASH("金额成交") ,
    SHARES("股数成交");

    OrderType(String value) {
        this.value = value;
    }
    private String value;

    public String getValue() {
        return value;
    }
}
