package com.yq.milk.constant.enumerate;

/**
 * Created by king on 17/6/1.
 */

public enum Condition {
    DEV {
        @Override
        public String dir() {
            return "dev";
        }
    },
    CN {
        @Override
        public String dir() {
            return "cn";
        }
    },
    GLOBAL {
        @Override
        public String dir() {
            return "global";
        }
    };

    public abstract String dir();
}
