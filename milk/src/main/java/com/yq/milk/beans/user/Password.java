package com.yq.milk.beans.user;

import com.yq.milk.constant.UserStore;

import static com.yq.milk.constant.UserStore.CURRENT_LOGIN_PWD;
import static com.yq.milk.constant.UserStore.CURRENT_TRADE_PWD;
import static com.yq.milk.constant.UserStore.P_START;
import static com.yq.milk.constant.UserStore.P_STOP;
import static com.yq.milk.constant.UserStore.getLoginPassword;
import static com.yq.milk.constant.UserStore.getTradePassword;
import static com.yq.milk.utils.God.getIntervalString;
import static com.yq.milk.utils.God.getRandomLoginPwd;
import static com.yq.milk.utils.God.getRandomTradePwd;

/**
 * Created by king on 2017/8/1.
 * 对密码的封装 , 可以取出文件中上次的密码 , 也可以获取随机密码
 */

public class Password {
    private String password;

    public Password(String password) {
        this.password = password;
    }

    public String getPassword() {
        if (password.startsWith(CURRENT_LOGIN_PWD)) {
            String userName = getIntervalString(password, P_START, P_STOP);
            password = getLoginPassword(userName);
            return password;
        }
        if (password.startsWith(CURRENT_TRADE_PWD)) {
            String userName = getIntervalString(password, P_START, P_STOP);
            password = getTradePassword(userName);
            return password;
        }
        if (password.startsWith(UserStore.RANDOM_LOGIN_PWD)) {
            String intervalString = getIntervalString(password, P_START, P_STOP);
            if ("".equals(intervalString)) {
                password = getRandomLoginPwd(0);
            }else{
                password = getRandomLoginPwd(Integer.valueOf(intervalString));
            }
            return password;
        }
        if (password.startsWith(UserStore.RANDOM_TRADE_PWD)) {
            String intervalString = getIntervalString(password, P_START, P_STOP);
            if ("".equals(intervalString)) {
                password = getRandomTradePwd(0);
            }else{
                password = getRandomTradePwd(Integer.valueOf(intervalString));
            }
            return password;
        }
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString(){
        return password;
    }
}
