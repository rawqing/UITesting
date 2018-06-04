package com.jlc.app.milk_mini.constant;

import android.app.Instrumentation;
import android.content.Context;
import android.support.test.InstrumentationRegistry;
import android.support.test.uiautomator.UiDevice;

import java.io.File;

/**
 * Created by king on 2016/9/26.
 * 定义一些环境常量
 */

public abstract class Conf {
    public final static String TAG = "jlc_default:";
    public final static Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
    public final static Context context = InstrumentationRegistry.getContext();
    public final static UiDevice uiDevice = UiDevice.getInstance(instrumentation);
    public final static String filesDir = instrumentation.getTargetContext().getFilesDir().getAbsolutePath()+ File.separator;
    public static final int VISIBILITY = 90;



    // 以下数据必须被重新修改
    // 被测apk的包名
    public  static String packageName = "";



}
