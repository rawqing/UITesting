package com.yq.milk.constant;


import com.trubuzz.trubuzz.shell.beautify.ToastElement;

import static com.trubuzz.trubuzz.test.R.string.accept_terms_of_service_hint;
import static com.trubuzz.trubuzz.test.R.string.captcha_format_error;
import static com.trubuzz.trubuzz.test.R.string.error_captcha_code;
import static com.trubuzz.trubuzz.test.R.string.incorrect_email_format;
import static com.trubuzz.trubuzz.test.R.string.incorrect_password_confirm;
import static com.trubuzz.trubuzz.test.R.string.incorrect_password_format;
import static com.trubuzz.trubuzz.test.R.string.incorrect_phone_format;
import static com.trubuzz.trubuzz.test.R.string.incorrect_sms_format;
import static com.trubuzz.trubuzz.test.R.string.incorrect_trade_password_format;
import static com.trubuzz.trubuzz.test.R.string.invalid_username_password;
import static com.trubuzz.trubuzz.test.R.string.reset_password_mail_sent;
import static com.trubuzz.trubuzz.test.R.string.sign_up_sms_auth_sent;
import static com.trubuzz.trubuzz.test.R.string.status_success;
import static com.trubuzz.trubuzz.test.R.string.user_not_exist;
import static com.trubuzz.trubuzz.test.R.string.watch_list_create_error;
import static com.trubuzz.trubuzz.utils.God.getString;

/**
 * Created by king on 16/11/4.
 */

public interface ToastInfo {
    ToastElement incorrect_password_confirm_toast =
            new ToastElement(getString("确认密码输入不一致" ,incorrect_password_confirm));

    ToastElement email_success_expect =
            new ToastElement(getString("重设密码信件已发送到您的邮箱，请查收并重置密码。" , reset_password_mail_sent));

    ToastElement incorrect_email_format_toast =
            new ToastElement(getString("邮箱地址格式不正确" ,incorrect_email_format)) ;

    ToastElement sms_sent_toast =
            new ToastElement(getString("短信验证码已发送" ,sign_up_sms_auth_sent));

    ToastElement incorrect_phone_format_toast =
            new ToastElement(getString("手机号格式不正确" ,incorrect_phone_format));

    ToastElement user_not_exist_toast =
            new ToastElement(getString("该用户不存在" ,user_not_exist));

    ToastElement incorrect_sms_format_toast =
            new ToastElement(getString("短信验证码格式不正确" ,incorrect_sms_format));

    ToastElement incorrect_password_format_toast =
            new ToastElement(getString("请输入6–16字符的大小写字母和数字组合" ,incorrect_password_format));

    ToastElement error_captcha_code_toast =
            new ToastElement(getString("验证码错误" ,error_captcha_code));

    ToastElement captcha_format_error_toast =
            new ToastElement(getString("验证码为六个数字" , captcha_format_error));

    ToastElement accept_terms_of_service_toast =
            new ToastElement(getString("请阅读并勾选同意服务条款以注册账号" , accept_terms_of_service_hint));

    ToastElement incorrect_account_or_pwd_toast =
            new ToastElement(getString("无效的账号或密码" ,invalid_username_password));

    ToastElement incorrect_trade_pwd_format_toast =
            new ToastElement(getString("请输入4~6位字母或数字" ,incorrect_trade_password_format));

    ToastElement status_success_toast =
            new ToastElement(getString("操作成功" ,status_success));

    ToastElement watchlist_existing_toast =
            new ToastElement(getString("该名称已存在，请修改板块名称。" ,watch_list_create_error));

}
