package com.yq.milk.constant.enumerate;


import com.yq.milk.constant.Config;

/**
 * Created by king on 2017/7/5.
 */

public enum ImageStrategy {
    FIX {
        @Override
        public String getImageCode() {
            return Config.FIXED_CODE;
        }
    },
    API {
        @Override
        public String getImageCode() {
            return null;
        }
    },
    OCR {
        @Override
        public String getImageCode() {
            return null;
        }
    };

    public abstract String getImageCode();
}
