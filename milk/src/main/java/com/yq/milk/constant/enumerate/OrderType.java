package com.yq.milk.constant.enumerate;

import static com.trubuzz.trubuzz.test.R.string.order_by_cash;
import static com.trubuzz.trubuzz.test.R.string.order_by_shares;
import static com.trubuzz.trubuzz.utils.God.getString;

/**
 * Created by king on 17/5/26.
 */

public enum OrderType {
    CASH(getString("金额成交" ,order_by_cash)) ,
    SHARES(getString("股数成交" ,order_by_shares));

    OrderType(String value) {
        this.value = value;
    }
    private String value;

    public String getValue() {
        return value;
    }
}
