package com.yq.milk.constant;

import android.support.annotation.Size;
import android.util.Log;

import com.esotericsoftware.yamlbeans.YamlException;
import com.esotericsoftware.yamlbeans.YamlReader;
import com.esotericsoftware.yamlbeans.YamlWriter;
import com.yq.milk.beans.user.Password;
import com.yq.milk.beans.user.UserName;
import com.yq.milk.utils.FileRw;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import static com.yq.milk.utils.God.getResources;


/**
 * Created by king on 2017/7/25.
 */

public class UserStore {
    private static String filePath = Env.filesDir + "user_store.yml";
    private static String email_max_key = "$email_max$";
    private static String phone_max_key = "$phone_max$";

    public static final String CURRENT_LOGIN_PWD = "$C_LP";
    public static final String CURRENT_TRADE_PWD = "$C_TP";
    public static final String RANDOM_LOGIN_PWD = "$R_LP";
    public static final String RANDOM_TRADE_PWD = "$R_TP";
    public static final String NEW_EMAIL_USER = "$N_EU";
    public static final String NEW_PHONE_USER = "$N_PU";
    public static final char P_START = '(';
    public static final char P_STOP = ')';
    public static final String separate = ",";


    static {
        // 如果手机中不存在该文件则将资源文件中的内容写入
        File file = new File(filePath);
        if (!file.exists()) {
//            InputStream in = getResources().openRawResource(com.trubuzz.trubuzz.test.R.raw.user_store);
//            FileRw.copyFile(in, filePath);
        }
    }


    /**
     * 获取当前邮箱用户最大后缀计数
     *      仅用于自动化创建用户
     * @return
     */
    public static int getEmailMax (){
        Object val = getValues(email_max_key);
        return getIntVal(val);
    }
    /**
     * 获取当前手机号用户最大后缀计数
     *      仅用于自动化创建用户
     * @return
     */
    public static int getPhoneMax (){
        Object val = getValues(phone_max_key);
        return getIntVal(val);
    }
    private static int getIntVal(Object val){
        if (val instanceof Integer) {
            return (int) val;
        }
        if (val instanceof String) {
            return Integer.valueOf((String) val);
        }
        return (int) val;
    }
    /**
     * 获取登录密码
     * @param key
     * @return 若未设置则返回 "" (空值)
     */
    public static String getLoginPassword(String key) {
        Object passwords = getValues(key);
        if (passwords instanceof String) {
            return (String)passwords;
        }
        if (passwords instanceof List) {
            return (String) ((List) passwords).get(0);
        }
        return "";
    }

    /**
     * 获取交易密码
     * @param key
     * @return 若未设置则返回 "" (空值)
     */
    public static String getTradePassword(String key) {
        Object passwords = getValues(key);
        if (passwords instanceof List) {
            if (((List) passwords).size() > 1) {
                return (String) ((List) passwords).get(1);
            }
        }
        return "";
    }

    /**
     * 获得所以password
     *      index:  0 > login password
     *              1 > trade password
     * @param key
     * @return
     */
    public static List getAllPassword(String key) {
        Object passwords = getValues(key);
        List allPwd = new ArrayList();
        if (passwords instanceof String) {
            allPwd.add(passwords);
            return allPwd;
        }
        if (passwords instanceof List) {
            return (List) passwords;
        }
        return allPwd;
    }
    /**
     * 从仓库中拿出值
     * @param key
     * @return [login password ,trade password] 组合
     */
    private static Object getValues(String key) {
        Map data = (Map) getData();
        if (data == null) return "";
        Object o = data.get(key);
        Log.i(Env.TAG, String.format("getValues: %s = %s",key ,o ));
        return o;
    }

    /**
     * 获取读取结果
     */
    private static Object getContact() {
        YamlReader reader = null;
        try {
            reader = new YamlReader(new FileReader(filePath));
            return reader.read();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }
    /**
     * 获取跟当前环境匹配的用户密码数据
     * @return
     */
    private static Object getData() {
        Object contact = getContact();
        if(contact == null || !(contact instanceof Map))    return null;

        Map read = (Map) contact;
        String name = Env.condition.name().toLowerCase();
        for (Object key : read.keySet()) {
            if (name.equals(key)) {
                return read.get(key);
            }
        }
        return null;
    }

    /**
     * 更新登录密码
     * @param user
     * @param value
     */
    public static void updateLoginPassword(String user, String value) {
        Log.i(Env.TAG, String.format("updateLoginPassword: user : %s ; new_login_password : %s", user, value));
        List<String> list = new ArrayList<>();
        list.add(value);
        list.add(null);
        updateValue(user ,list);
    }

    public static void updateLoginPassword(String user, Password value) {
        updateLoginPassword(user, value.toString());
    }

    /**
     * 更新交易密码
     * @param user
     * @param value
     */
    public static void updateTradePassword(String user, String value) {
        List<String> list = new ArrayList<>();
        list.add(null);
        list.add(value);
        updateValue(user ,list);
    }
    public static void updateTradePassword(String user, Password value) {
        updateLoginPassword(user ,value.toString());
    }
    public static void addUser(String userName , String... pwds) {
        if (pwds.length == 1) {
            List<String> list = new ArrayList<>();
            list.add(pwds[0]);
            list.add(null);
            updateValue(userName ,list);
        }
        if (pwds.length >= 2) {
            List<String> list = new ArrayList<>();
            list.add(pwds[0]);
            list.add(pwds[1]);
            updateValue(userName ,list);
        }
    }

    /**
     * 增加一个user
     * @param userName
     * @param pwds
     */
    public static void addUser(UserName userName , Password ... pwds) {
        addUser(userName.toString() , Arrays.toString(pwds));
    }
    public static void addUser(UserName userName , String... pwds) {
        addUser(userName.toString() , Arrays.toString(pwds));
    }

    /**
     * 更新 用户递增最大值
     * @param key
     * @param value
     */
    public static void updateUserMax(String key, Integer value){
        writeValue(key ,value ,false);
    }
    /**
     * 更新value
     * @param key
     * @param value
     */
    private static void updateValue(String key, @Size(2)List<String> value) {
        writeValue(key ,value ,true);
    }

    /**
     * 写入键值 , key 存在则为更新 , 不存在则为添加
     * @param key
     * @param value
     */
    private static void writeValue(String key, Object value , boolean change) {
        YamlWriter writer = null;
        try {
            Object contact = getContact();
            if(contact == null || !(contact instanceof Map))    return ;

            Map read = (Map) contact;
            String name = Env.condition.name().toLowerCase();
            for (Object cond : read.keySet()) {
                if (name.equals(cond)) {
                    Map data = (Map) read.get(cond);
                    // 更新值 , 若当前 key 不存在则为添加
                    Object old = data.get(key);
                    if (change) {
                        value = exchangeValue(old ,value);
                    }
                    data.put(key, value);
                    break;
                }
            }

            writer = new YamlWriter(new FileWriter(filePath));
            writer.write(read);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if(writer != null)
                try {
                    writer.close();
                } catch (YamlException e) {
                    e.printStackTrace();
                }
        }
    }

    /**
     * 交换新旧值
     * @param old
     * @param values
     * @return
     */
    private static Object exchangeValue(Object old , List<String> values){
        if (old instanceof List) {
            List<String> newVal = new ArrayList<>();
            for (int i=0; i< values.size(); i++) {
                if(values.get(i) == null){
                    if(i < ((List) old).size()){
                        newVal.add(i , (String) ((List) old).get(i));
                    }else
                        newVal.add(i ,"$error");
                }

                else{    newVal.add(i ,values.get(i));}
            }
            return newVal;
        }
        if (old instanceof String) {
            if (values.get(1) == null) {
                return values.get(0);
            }
        }
        return values;
    }

    private static Object exchangeValue(Object old, Object val){
        if (old == null) {
            return val;
        }
        if (val instanceof List) {
            return exchangeValue(old, (List<String>) val);
        }
        return val;
    }

}
