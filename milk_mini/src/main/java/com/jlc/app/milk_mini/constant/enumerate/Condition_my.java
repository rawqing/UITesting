package com.jlc.app.milk_mini.constant.enumerate;

import com.jlc.app.milk_mini.bTest.User;

/**
 * Created by king on 17/6/1.
 */
@Deprecated
public enum Condition_my {
    DEV {
        @Override
        public String dir() {
            return "dev";
        }

        @Override
        public User getBaseUser() {
            return new User("","");
        }
    },
    TEST {
        @Override
        public String dir() {
            return "test";
        }

        @Override
        public User getBaseUser() {
            return new User("","");
        }
    },
    PRE {
        @Override
        public String dir() {
            return "pre";
        }

        @Override
        public User getBaseUser() {
            return new User("","");
        }
    },
    OL {
        @Override
        public String dir() {
            return "ol";
        }

        @Override
        public User getBaseUser() {
            return new User("","");
        }
    };

    public abstract String dir();
    public abstract User getBaseUser();

}
