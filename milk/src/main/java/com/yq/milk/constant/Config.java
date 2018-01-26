package com.yq.milk.constant;


import com.yq.milk.constant.enumerate.ImageStrategy;
import com.yq.milk.utils.DoIt;
import com.yq.milk.utils.FileRw;
import com.yq.milk.utils.Judge;
import com.yq.milk.utils.Kvp;
import com.yq.milk.utils.MReflect;

import java.lang.reflect.Field;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import static com.yq.milk.utils.God.getResources;


/**
 * Created by king on 2017/7/17.
 */

public class Config {
    private static Map conf ;
    private static String[] skips = {"ad_log_default_cookie"};
    static {
        int id = 0;
        switch (Env.condition) {
            case DEV:
//                id = com.trubuzz.trubuzz.test.R.raw.config_dev;
                break;
            case CN:
//                id = com.trubuzz.trubuzz.test.R.raw.config_cn;
                break;
            case GLOBAL:
//                id = com.trubuzz.trubuzz.test.R.raw.config_global;
                break;
        }
        List list = FileRw.readYamlFile(getResources().openRawResource(id));
        conf = (Map) list.get(0);

        Field[] fields = Config.class.getFields();
        Field.setAccessible(fields ,true);
        for (Field field : fields) {
            String name = field.getName();
            if (Judge.in(skips, name)) {
                continue;
            }
            Class<?> type = field.getType();
            Object data = DoIt.castObject(type, conf.get(name));
            MReflect.setFieldValue(field ,null ,data);
        }

    }

    /*** 默认用户登录 ***/
    public static String hasBrokerUser ;
    public static String hasBrokerPwd ;
    public static String notBrokerUser ;
    public static String notBrokerPwd ;
    public static String tradePwd ;

    /*** 手续费相关 ***/
    public static BigDecimal usMinFee ;
    public static BigDecimal usPerFee ;
    public static BigDecimal usMaxFee ;
    public static BigDecimal hkMinFee ;
    public static BigDecimal hkPerFee ;

    /*** admin相关 ***/
    public static String ad_log_cookie_key ;
    public static String ad_log_cookie_value ;
    public static Kvp<String,String> ad_log_default_cookie = new Kvp<>(ad_log_cookie_key, ad_log_cookie_value);
    public static String ad_log_cookie_file ;
    public static String ad_sms_log_url ;
    public static String ad_login_url ;
    public static String ad_domain ;
    public static String ad_path ;
    public static String ad_user_mail ;
    public static String ad_user_password ;

    /*** 图像验证码 ***/
    public static ImageStrategy CURRENT_IMAGE_STRATEGY ;
    public static String FIXED_CODE ;

    /*** 默认国别码 ***/
    public static String default_country_code;

    /*** 自动化生成用户名 ***/
    public static String email_name_prefix ;    // 邮箱前缀
    public static String email_name_postfix ;   // 邮箱后缀
    public static String phone_name_prefix ;    // cn 手机号前缀(前7位数) , 后四位递增
    public static Integer phone_name_replenish ;    // 手机号需补充位数
}
