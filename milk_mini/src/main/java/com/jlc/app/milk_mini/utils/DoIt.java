package com.jlc.app.milk_mini.utils;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.support.test.espresso.Espresso;
import android.support.test.espresso.IdlingResource;
import android.support.test.espresso.action.CoordinatesProvider;
import android.support.test.uiautomator.UiDevice;
import android.util.Log;
import android.view.View;

import com.jlc.app.milk_mini.constant.Conf;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Stack;

/**
 * Created by king on 2016/9/9.
 * 功能: 做一些事情
 */
public class DoIt {
    private static final String TAG = "jlc_DoIt";

    private static Stack o_idlingResource = null; //某些方法私有的属性

    /**
     * 注册idlingResource 对注册的对象圧栈处理,方便之后注销
     *
     * @param idlingResources
     * @param <T>
     */
    @SafeVarargs
    public static <T extends IdlingResource> void regIdlingResource(T... idlingResources) {
        if (o_idlingResource == null) o_idlingResource = new Stack<IdlingResource>();
        Espresso.registerIdlingResources(idlingResources);
        for (IdlingResource ir : idlingResources) o_idlingResource.push(ir);
    }

    /**
     * 注销一个idlingResources实例,并出栈
     */
    public static void unRegIdlingResource() {
        if (o_idlingResource.empty()) return;
        Espresso.unregisterIdlingResources((IdlingResource) o_idlingResource.pop());
        if (o_idlingResource.empty()) o_idlingResource = null;
    }

    @SafeVarargs
    public static <T extends IdlingResource> void unRegIdlingResource(T... idlingResources) {
        Espresso.unregisterIdlingResources(idlingResources);
        for (IdlingResource ir : idlingResources) {
            o_idlingResource.remove(ir);
        }
        if (o_idlingResource.empty()) o_idlingResource = null;
    }

    /**
     * 注销当前所有的idlingResources实例
     * 此方法需慎用,如果有控制外的实例在栈中,也将被注销.
     */
    public static void unAllRegIdlingResource() {
        while (true) {
            if (o_idlingResource == null) return;
            unRegIdlingResource();
        }
    }

    /***********-----------------------------屏幕截图-----------------------------************/

    /**
     *
     * @param activity 必须使用捕获到的当前activity , rule中预设的activity在跨activity时会无效
     * @param imgName
     * @return String file name
     * @deprecated espresso 不能截到toast 。use{@link #takeScreenshot(UiDevice, File)}
     */
    public static String takeScreenshot(Activity activity, String imgName){
        return outPutScreenshot(activity, takeBitmap(activity),imgName);
    }

    /**
     * 截取bitmap
     * @param activity
     * @return
     * @deprecated espresso 不能截到toast 。use{@link #takeScreenshot(UiDevice, File)}
     */
    public static Bitmap takeBitmap(Activity activity){
        View scrView = activity.getWindow().getDecorView().getRootView();
        scrView.setDrawingCacheEnabled(true);
        Bitmap bitmap = Bitmap.createBitmap(scrView.getDrawingCache());
        scrView.setDrawingCacheEnabled(false);

        Log.i(TAG, "takeBitmap: 截取屏幕");
        return bitmap;
    }

    /**
     * 将bitmap写入磁盘 , 并返回文件名
     * @param activity
     * @param bitmap
     * @param imgName
     * @return
     * @deprecated espresso 不能截到toast 。use{@link #takeScreenshot(UiDevice, File)}
     */
    public static String outPutScreenshot(Activity activity , Bitmap bitmap , String imgName){
        String fileName  = "";
        FileOutputStream out = null;
        try {
            fileName = makeFileName(imgName)+".png" ;
            out = activity.openFileOutput(fileName, Activity.MODE_PRIVATE);
            bitmap.compress(Bitmap.CompressFormat.PNG, 90, out);

            out.flush();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        return Conf.filesDir+fileName;
    }
    public static String outPutScreenshot(Bitmap bitmap , File file){
        FileOutputStream out = null;
        try {
            out = new FileOutputStream(file);
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, out);

            out.flush();
        } catch (Exception e) {
            e.printStackTrace();
        }finally {
            try {
                if (out != null) {
                    out.close();
                }
            } catch (Exception exc) {
                exc.printStackTrace();
            }
        }
        return file.getAbsolutePath();
    }
    /**
     * 为文件名加上时间戳
     * @param pratName
     * @return
     */
    public static String makeFileName(String pratName){
        Date date = new Date();
        String time = God.getDateFormat(date,"yyMMdd", Locale.CHINA);
        pratName = pratName == null ? "" : pratName;
        return "jlc_" + pratName + "_" + time + "_" + date.getTime();
    }

    public static String writeFileData(String data , String fileName){
        BufferedWriter writer = null;
        File file = null;
        try {
            file = new File(Conf.filesDir + makeFileName("json_"+ fileName) + ".json");
            if (file.exists()){
                file.createNewFile();
            }
            writer = new BufferedWriter(new FileWriter(file ,true));
            writer.write(data);
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, "writeFileDataError: ",e );
        } finally {
            if(writer != null){
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return file.getAbsolutePath();
    }

    public static String takeScreenshot(UiDevice uiDevice, File file){
        uiDevice.takeScreenshot(file);
        return file.getAbsolutePath();
    }
    public static String takeScreenshot(UiDevice uiDevice, File file , float scale, int quality){
        uiDevice.takeScreenshot(file, scale,  quality);
        return file.getAbsolutePath();
    }
    public static String takeScreenshot(UiDevice uiDevice, String imageName){
        String localName = makeFileName( "ps_"+ imageName) + ".png";
        String fileAbsolutePath = Conf.filesDir+ localName;

        uiDevice.takeScreenshot(new File(fileAbsolutePath));
        return localName;           //修改为局部名称(文件名) , 因为跟json文件在同一目录
    }


    /*************************/
    public static void sleep(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    /*****************************/
    public static boolean delFile(String fileName){
        try {
            if(fileName == null || fileName.isEmpty()) return true;

            new File(Conf.filesDir+fileName).delete();
            Log.i(TAG, "delFile:  删除临时文件 :"+fileName);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /***********************************/
    public static Date dateValueOf(String date, String format) {
        SimpleDateFormat sdf = new SimpleDateFormat(format);
        try {
            return sdf.parse(date);
        } catch (ParseException e) {
            e.printStackTrace();
            return null;
        }
    }

    /********************************/

    public static String string2ASCII(String string){
        String newString = "";
        char[] chars = string.toCharArray();
        for(char c: chars){
            newString = newString + Integer.valueOf(c) + ",";
        }
        return newString;
    }

    /**
     * 进制转换 , 尚未提供10进制以下的转换功能
     * @param i 十进制数
     * @param scale must be 10 < scale < 71
     * @return 指定进制的字符串
     */
    public static String conversionScale(long i , int scale){
        char[] chars = new char[scale];
        char ch = 'A';
        char nu = '0';
        long m = 0;  //商
        long y = 0;  //余
        int index = chars.length;
        long c = 0;  //差

        do{
            m = i / scale;
            y = i % scale;
            c = y - 10;
            chars[--index] = (char) (c < 0 ? nu + y : ch + c);
            i = m;
        }while (m!=0);

        return new String(chars ,index ,chars.length-index);
    }

    /**
     * 为图片添加水印或文字
     * @param target 被添加的图片
     * @param mark 水印或文字
     * @return 添加过水印或文字的图片
     */
    public static Bitmap createWatermark(Bitmap target, String mark , int color) {
        int w = target.getWidth();
        int h = target.getHeight();

        Bitmap bmp = Bitmap.createBitmap(w, h, Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(bmp);

        Paint p = new Paint();
        p.setStyle(Paint.Style.STROKE);     //设置画笔
        p.setStrokeWidth(5);                //画笔大小
        p.setTextAlign(Paint.Align.CENTER);

        // 水印的颜色
        p.setColor(color);

        // 水印的字体大小
        p.setTextSize(80);

        p.setAntiAlias(true);// 去锯齿

        canvas.drawBitmap(target, 0, 0, p);

        // 在左边的中间位置开始添加水印
        canvas.drawText(mark, w/2, h / 2, p);

        canvas.save(Canvas.ALL_SAVE_FLAG);
        canvas.restore();

        return bmp;
    }

    /**
     * 如果用long类型 则截取其16进制数的后8位作为颜色.
     * @param target
     * @param mark
     * @param nu
     * @return
     */
    public static Bitmap createWatermark(Bitmap target, String mark , long nu) {
        String str = toHexUpString(nu);
        str = str.substring(str.length()-8 ,str.length());
        int color = hexString2int(str);
        return createWatermark(target ,mark ,color);
    }

    /**
     * long 转16进制字符串大写格式
     * @param number
     * @return
     */
    public static String toHexUpString(long number){
        return Long.toHexString(number).toUpperCase();
    }

    /**
     * 16进制字符串转int格式
     * @param hex
     * @return
     */
    public static int hexString2int(String hex){
        Long l =  Long.parseLong(hex ,16);
        return l.intValue();
    }

    public static boolean notEmpty(Object o){
       return o !=null;
    }
    public static boolean notEmpty(Object[] o) {
        return o != null && o.length > 0;
    }
    public static boolean notEmpty(List o) {
        return o != null && !o.isEmpty();
    }
    public static boolean notEmpty(Map o) {
        return o != null && !o.isEmpty();
    }

    public static Object[] list2array(List list) {
        int size = list.size();
        if (size < 1) {
            return null;
        }
        Object[] es =  new Object[size];
        for(int i=0; i<size ; i++) {
            es[i] = list.get(i);
        }
        return es;
    }

    /**
     * 类型转换
     * @param clz
     * @param data
     * @return
     */
    public static Object castObject(Class clz, Object data) {
        if (clz == Integer.class) {
            if(data instanceof Integer) return data;
            return Integer.valueOf((String)data);
        }
        if (clz == String.class) {
            if(data instanceof String)return data;
            return String.valueOf(data);
        }
        if (clz == Float.class) {
            data = String.valueOf(data);
            return Float.valueOf((String)data);
        }
        if (clz == Double.class) {
            data = String.valueOf(data);
            return Double.valueOf((String)data);
        }
        if (clz == Long.class) {
            data = String.valueOf(data);
            return Long.valueOf((String)data);
        }
        if (clz == BigDecimal.class) {
            if (data instanceof BigDecimal) return data;
            data = String.valueOf(data);
            return new BigDecimal((String)data);
        }
        if (clz.isEnum()) {
            return Enum.valueOf(clz, (String) data);
        }
        System.out.println("jlc_没有合适的类型 : "+ clz);
        return data;
    }

/**************************** 由Google API 中摘出来的封装方法 *****************************/
    /**
     * see {@link android.support.test.espresso.action.GeneralLocation#translate(CoordinatesProvider, float, float)}
     * @param coords
     * @param dx
     * @param dy
     * @return
     */
    public static CoordinatesProvider translate(final CoordinatesProvider coords,
                                                final float dx, final float dy) {
        return new CoordinatesProvider() {
            @Override
            public float[] calculateCoordinates(View view) {
                float xy[] = coords.calculateCoordinates(view);
                xy[0] += dx * view.getWidth();
                xy[1] += dy * view.getHeight();
                return xy;
            }
        };
    }


    public static <K,V> Map<K,V> createMap(K k , V v){
        Map<K,V> map = new HashMap<>();
        map.put(k, v);
        return map;
    }
}
