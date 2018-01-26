package com.yq.milk.beans.user;


import static com.yq.milk.constant.Config.email_name_postfix;
import static com.yq.milk.constant.Config.email_name_prefix;
import static com.yq.milk.constant.Config.phone_name_prefix;
import static com.yq.milk.constant.Config.phone_name_replenish;
import static com.yq.milk.constant.UserStore.NEW_EMAIL_USER;
import static com.yq.milk.constant.UserStore.NEW_PHONE_USER;
import static com.yq.milk.constant.UserStore.getEmailMax;
import static com.yq.milk.constant.UserStore.getPhoneMax;

/**
 * Created by king on 2017/8/3.
 */

public class UserName {
    private String userName;

    public UserName(String userName) {
        this.userName = userName;
    }

    public String getUserName() {
        if (NEW_EMAIL_USER.equals(userName)) {
            int max = getEmailMax() + 1;
            userName = email_name_prefix + String.valueOf(max) + email_name_postfix;
            return userName;
        }
        if (NEW_PHONE_USER.equals(userName)) {
            int max = getPhoneMax() + 1;
            String mstr = String.valueOf(max);
            StringBuffer str = new StringBuffer();
            for(int i=0 ;i < phone_name_replenish - mstr.length(); i++) {
                str.append('0');
            }
            userName = phone_name_prefix + new String(str) + max;
            return userName;
        }
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public String toString() {
        return userName;
    }
}
