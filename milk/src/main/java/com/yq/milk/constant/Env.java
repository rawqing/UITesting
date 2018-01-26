package com.yq.milk.constant;

import android.app.Instrumentation;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import com.trubuzz.trubuzz.constant.enumerate.Condition;

import java.io.File;

/**
 * Created by king on 2016/9/26.
 * 定义一些环境常量
 */

public class Env {
    public final static String TAG = "jcd_default:";
    // app 的运行环境[DEV ,CN ,GLOBAL]
    public static final Condition condition = Condition.DEV;
    public final static Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    public final static UiDevice uiDevice = UiDevice.getInstance(instrumentation);

    public final static String packageName = "com.trubuzz.trubuzz";
    public final static String testPackageName = "com.trubuzz.trubuzz.test";
//    public final static String filesDir = "/data/data/"+packageName + "/files/";
    public final static String filesDir = instrumentation.getTargetContext().getFilesDir().getAbsolutePath()+ File.separator;
    public final static String suiteRegisterKey = "SUITE_REGISTER_KEY";

    // email 的正则规则
    public final static String emailRegex = "\\w[-\\w.+]*@([A-Za-z0-9][-A-Za-z0-9]+\\.)+[A-Za-z]{2,14}";

    // 测试数据根目录
    public final static String test_data_root_dir = "test_data";

    // 测试报告名称
    public static final String reportName = "聚财道 app 2.0 Android 自动化测试报告";

    public static final String cacheHeadImage = "head.png";

    public static final int VISIBILITY = 90;
    public static final String watchlistKey = "name";

    /*** 参数分割符 ***/
    public static final String p_s = "~&";

}
