package com.yq.milk.utils;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedWriter;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.CookieHandler;
import java.net.CookieManager;
import java.net.CookiePolicy;
import java.net.HttpCookie;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.yq.milk.constant.Config.ad_domain;
import static com.yq.milk.constant.Config.ad_path;


/**
 * Created by king on 2017/6/21.
 */

public class DoHttp {
    private static String TAG = "jcd_" + DoHttp.class.getSimpleName();
    public static final String cookie_key = "cookie";
    public static final String html_key = "html";

    public static Map<String, Kvp<String,String>> doGet(final String path ) {
        Map<String,Kvp<String,String>> map = new HashMap<>();
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        HttpURLConnection conn;
        try {
            URL url = new URL(path.trim());
            //打开连接
            conn = (HttpURLConnection) url.openConnection();
            int code = conn.getResponseCode();
            if(200<= code && code < 400){

                // 得到输入流
                InputStream in =conn.getInputStream();
                ByteArrayOutputStream baos = new ByteArrayOutputStream();
                byte[] buffer = new byte[1024];
                int len = 0;
                while(-1 != (len = in.read(buffer))){
                    baos.write(buffer,0,len);
                    baos.flush();
                }
                in.close();
                baos.close();
                List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
                for (HttpCookie hc : cookies) {
                    System.out.println(String.format("set cookie -> %s : %s" ,hc.getName() ,hc.getValue()));
                    if (hc.getMaxAge() > 0) {
                        map.put(cookie_key, new Kvp<>(hc.getName() ,hc.getValue()));
                    }
                }
                map.put(html_key, new Kvp<>("html", baos.toString("utf-8")));
                return map;
            }
        }  catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 带 cookie 的post请求
     * @param path
     * @param cookie
     * @param params
     * @return
     */
    public static Map<String, Kvp<String,String>> doPost(final String path, Kvp<String, String> cookie , List<Kvp<String, String>> params) {
        Map<String ,Kvp<String,String>> map = new HashMap<>();
        URL url = null;
        CookieManager cookieManager = new CookieManager();
        cookieManager.setCookiePolicy(CookiePolicy.ACCEPT_ALL);
        CookieHandler.setDefault(cookieManager);
        int responseCode = 0;
        String responseMessage = "";
        try {
            url = new URL(path);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            HttpCookie coo = new HttpCookie(cookie.getKey() ,cookie.getValue());
            coo.setDomain(ad_domain);
            coo.setPath(ad_path);
            coo.setVersion(0);
            cookieManager.getCookieStore().add(new URI("https://tb2devadmin.trubuzz.com"), coo);

            // 发送POST请求必须设置如下两行
            conn.setDoInput(true);
            conn.setDoOutput(true);
            conn.setChunkedStreamingMode(0);
//            conn.setRequestMethod("POST");// 提交模式
            conn.setConnectTimeout(10000);//连接超时 单位毫秒
            conn.setReadTimeout(2000);//读取超时 单位毫秒
            // 获取URLConnection对象对应的输出流
            OutputStream os = conn.getOutputStream();
            BufferedWriter writer = new BufferedWriter(
                    new OutputStreamWriter(os, "UTF-8"));
            // 发送请求参数
            String postParams = getPostParams(params);
            if(!postParams.isEmpty()) {
                writer.write(postParams);//post的参数 xx=xx&yy=yy
            }
            // flush输出流的缓冲
            writer.flush();
            writer.close();
            os.close();
            conn.connect();
            //开始获取数据
            responseCode = conn.getResponseCode();
            responseMessage = conn.getResponseMessage();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            ByteArrayOutputStream bos = new ByteArrayOutputStream();
            List<HttpCookie> cookies = cookieManager.getCookieStore().getCookies();
            for (HttpCookie hc : cookies) {
//                System.out.println(String.format("set cookie -> %s : %s" ,hc.getName() ,hc.getValue()));
                if (hc.getMaxAge() > 0) {
                    map.put(cookie_key, new Kvp<>(hc.getName() ,hc.getValue()));
                }
            }
            int len;
            byte[] arr = new byte[1024];
            while((len=bis.read(arr))!= -1){
                bos.write(arr,0,len);
                bos.flush();
            }
            bos.close();
            map.put(html_key, new Kvp<>("html", bos.toString("utf-8")));
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            Log.e(TAG, String.format("doPost:%s ; status = %s ", responseMessage ,responseCode),e );
        }
        return null;
    }

    private static String getPostParams(List<Kvp<String, String>> params) {
        String post = "";
        if (params == null) {
            return "";
        }
        boolean first = true;
        for (Kvp<String, String> p : params) {
            if (first) {
                first = false;
            } else {
                post += " &";
            }
            post += p.getKey() + "=" + p.getValue();
        }
        return post;
    }

}
