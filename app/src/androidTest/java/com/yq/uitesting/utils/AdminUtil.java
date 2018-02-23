package com.yq.uitesting.utils;

import android.support.annotation.Nullable;
import android.util.Log;


import com.yq.milk.utils.DoHttp;
import com.yq.milk.utils.DoIt;
import com.yq.milk.utils.FileRw;
import com.yq.milk.utils.God;
import com.yq.milk.utils.HtmlParser;
import com.yq.milk.utils.Kvp;
import com.yq.uitesting.Config;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;

import static com.yq.uitesting.Config.ad_log_cookie_file;
import static com.yq.uitesting.Config.ad_log_cookie_key;
import static com.yq.uitesting.Config.ad_log_default_cookie;
import static com.yq.milk.utils.FileRw.readProperty;
import static com.yq.uitesting.Config.ad_login_url;
import static com.yq.uitesting.Config.ad_user_mail;
import static com.yq.uitesting.Config.ad_user_password;


/**
 * Created by king on 2017/6/21.
 * 专注后端解析
 */

public class AdminUtil {
    private final String TAG = "jcd_" + AdminUtil.class.getSimpleName();
    private final Kvp<String, String> session = getLogCookie(ad_log_cookie_file);
    private final String tbody_select = "table.table.table-striped.table-hover>tbody";
    // 需format %s
    private final String sms_code_td = "tr>td:contains(%s) + td";
    private final String datetime_td = "tbody>tr>td.text-center:matches" +
            "(^\\d{4}(\\-|\\/|.)\\d{1,2}\\1\\d{1,2}\\s\\d{2}.\\d{2}.\\d{2}$)";
    private final String login_select = "form.form-signin";
    private final String page_next = "li.paginate_button.next";
    private final String page_str = "?page=";
    private final String format = "yyyy-MM-dd HH:mm:ss";
    private final int firstPage = 1;
    private final int code_quantity = 6;
    private int retry = 0;
    private final int retryMax = 3;
    private long nowTime = new Date().getTime();

    private final String token_key = "_token";
    private final String email_key = "email";
    private final String password_key = "password";

    private final String token_select = "form.form-signin>input[name=_token]";
    // 认为的登录成功的标志(隐藏/显示 菜单的button ), 若未自动重定向则可能失败
    private final String isLogin_token = "div.header-section>a.toggle-btn";

    /**
     * 获取验证码 with 手机号
     * @param phoneNumber
     * @return
     */
    public String getCurrentSmsCode(String phoneNumber){
        nowTime = new Date().getTime();
        String sms_select = String.format(sms_code_td, phoneNumber);
        Element smsCodeTd = getSmsCodeTd(Config.ad_sms_log_url, session, tbody_select, sms_select, firstPage);

        return getSmsCode(smsCodeTd);
    }

    /**
     * 获取验证码字符串
     * @param codeTd
     * @return
     */
    private String getSmsCode(Element codeTd){
        String code_text = codeTd.text();
        return God.getNumberFromString(code_text, this.code_quantity);
    }

    /**
     * 获取验证码所在的 td 元素
     * @param url
     * @param cookie
     * @param tbodySelector
     * @param codeSelector
     * @param pageNow default must be 1 .
     * @return
     */
    @Nullable
    private Element getSmsCodeTd(String url , Kvp<String,String> cookie , String tbodySelector
            , String codeSelector , int pageNow) {
        Element tbody = getTbody(url + page_str + pageNow, cookie, tbodySelector);
        Element firstCode = tbody.select(codeSelector).first();
        // 第一页出现则直接返回
        if (firstCode != null) {
            return firstCode;
        }
        if (pageNow >= getMaxPages(tbody)) {
            return null;
        }
        // 将当前时间前推5分钟 , 若最后一行的时间比此还小则表示后面也没有了.
        if (nowTime - 1000 * 300 > getLastLineDate(tbody, format)) {
            return null;
        }
        // 否则继续向下一页寻找
        return getSmsCodeTd(url, cookie, tbodySelector, codeSelector ,++pageNow);
    }

    /**
     * 获取tbody元素
     * @param url
     * @param cookie
     * @param tbodySelector
     * @return
     */
    @Nullable
    private Element getTbody(String url , Kvp<String,String> cookie  , String tbodySelector ) {
        if (retry > retryMax) {
            Log.e(TAG, String.format("getTbody: 尝试登录%s次失败 , 请检查原因", retry));
            retry = 0;
            return null;
        }
        Document doc = HtmlParser.doGetDoc(url, cookie);
        Element tbody = doc.select(tbodySelector).first();
        if (tbody != null) {
            return tbody;
        }
        // 未获取到 tbody , 查看是否cookie失效
        Elements login_form = doc.select(login_select);
        // 如果也没有登录form , 则需检查html文本
        if (login_form.isEmpty()) {
            Log.e(TAG, String.format("getPhoneTr: 解析html页面错误 , 找不到目标节点 . html = %s", doc.html()));
            return null;
        }else{
            // 重新登录后获取cookie ,
            retry ++;
            String token = get_token(doc);
            Kvp<String, String> nowCookie = getNowCookie(ad_login_url);
            return getTbody(url, nowCookie, tbodySelector);
        }
    }

    /**
     * 重登陆并获取cookie , 保存cookie
     * @param loginUrl
     * @return
     */
    public Kvp<String,String> getNowCookie(String loginUrl){
        Map<String, Kvp<String, String>> get_res = DoHttp.doGet(loginUrl);
        String html = get_res.get(DoHttp.html_key).getValue();
        Kvp<String, String> cookie = get_res.get(DoHttp.cookie_key);
        String token = get_token(html);

        List<Kvp<String, String>> params = new ArrayList<>();
        params.add(new Kvp<>(token_key, token));
        params.add(new Kvp<>(email_key, ad_user_mail));
        params.add(new Kvp<>(password_key, ad_user_password));
        Map<String, Kvp<String, String>> post_res = DoHttp.doPost(loginUrl, cookie, params);
        String isLogin_html = post_res.get(DoHttp.html_key).getValue();
        Kvp<String, String> newCookie = post_res.get(DoHttp.cookie_key);

        if (! isLogin(isLogin_html)) {
            Log.e(TAG, String.format("getNowCookie: home页面未发现成功标志 ,可能登录失败 .\n html : %s",isLogin_html ));
        }
        // 将cookie更新至properties文件持久化
        ArrayList<Kvp<String, String>> kvps = new ArrayList<>();
        kvps.add(newCookie);
        FileRw.saveProperties(Config.ad_log_cookie_file, kvps);
        return newCookie;
    }

    /**
     * 获取登录页面隐藏的token
     * @param doc
     * @return
     */
    private String get_token(Document doc){
        Element token_input = doc.select(token_select).first();
        return token_input.attr("value");
    }
    private String get_token(String html){
        Document doc = Jsoup.parse(html);
        return get_token(doc);
    }

    /**
     * 将末尾的日期转换成Date类型 并 返回
     * @param tbody
     * @param format
     * @return
     */
    private long getLastLineDate(Element tbody ,String format){
        Element td = tbody.select(datetime_td).last();

        String s = td.text();
        return DoIt.dateValueOf(s, format).getTime();
    }

    /**
     * 获取最大页数
     * @param tbody
     * @return
     */
    private int getMaxPages(Element tbody){
        Element root = tbody.parents().last();
        Element next = root.select(page_next).first();
        Element maxPage = next.previousElementSibling();
        return Integer.valueOf(maxPage.text());
    }

    private boolean isLogin(String html) {
        Document doc = Jsoup.parse(html);
        Element toggle = doc.select(isLogin_token).first();
        return toggle != null;
    }

    /**
     * 获取预置的cookie , 先从设备上的配置文件上取 ,
     *      若没取到则从预置类中取
     * @param fileName
     * @return
     */
    public static Kvp<String, String> getLogCookie(String fileName){
        String cvalue = readProperty(fileName, ad_log_cookie_key);
        if (cvalue.isEmpty()) {
            return ad_log_default_cookie;
        }
        return new Kvp<>(ad_log_cookie_key, cvalue);
    }
}
