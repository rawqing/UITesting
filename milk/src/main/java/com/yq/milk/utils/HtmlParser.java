package com.yq.milk.utils;

import android.util.Log;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;

import java.io.IOException;

/**
 * Created by king on 2017/6/21.
 */

public class HtmlParser {
    private static String TAG = "jcd_" + HtmlParser.class.getSimpleName();

    public static Document doGetDoc(String url , Kvp<String,String> cookie) {
        Document doc = null;
        try {
            doc = Jsoup.connect(url)
                    .cookie(cookie.getKey(), cookie.getValue())
                    .get();
            Log.i(TAG, String.format("doGetDoc: get %s successful .", url));
        } catch (IOException e) {
            Log.e(TAG, String.format("doGetDoc: get %s error .", url), e);
        }
        return doc;
    }

    public String getHtmlText(String url , Kvp<String,String> cookie){
        Document doc = doGetDoc(url, cookie);
        if (doc != null) {
            return doc.html();
        }
        return "";
    }

//    public void d(){
//        Document doc = this.doGetDoc(Conf.ad_sms_log_url, Conf.ad_log_cookie);
//        Elements trs = doc.select("table.table.table-striped.table-hover>tbody>tr:contains(11811110001)");
//    }
}
