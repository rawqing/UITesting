package com.yq.milk.bTest;

import android.util.Log;
import android.view.View;

import com.yq.milk.shell.AdViewInteraction;
import com.yq.milk.utils.DoIt;
import com.yq.milk.utils.Registor;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.ClassRule;
import org.junit.Rule;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.support.test.espresso.action.ViewActions.click;
import static com.yq.milk.constant.Env.uiDevice;
import static com.yq.milk.utils.DoIt.sleep;
import static com.yq.milk.utils.DoIt.unAllRegIdlingResource;

/**
 * Created by king on 2016/9/23.
 * 主要功能用于测试生命周期的监控 ; 测试错误截图 ; 测试报告数据收集等
 * 所有的Test子类必须继承该类
 */

public class BaseTest {
    protected final String TAG = "jcd_" + this.getClass().getSimpleName();
    private final String BTAG = "jcd_BaseTest";
    private List<String> compareImageNames = new ArrayList<>();

    protected final String OL_PIN = "origin_login_password";
    protected final String OT_PIN = "origin_trade_password";
    protected final String NL_PIN = "new_login_password";
    protected final String NT_PIN = "new_trade_password";

    @ClassRule
    public static ClassWatcherAdvance classWatcherAdvance = new ClassWatcherAdvance();
    @Rule
    public TestWatcherAdvance testWatcherAdvance = new TestWatcherAdvance(classWatcherAdvance.getTestClass() , this);

    protected BaseTest(){}

    @Before
    public void setUp() {
        Registor.reg(BaseTest.class.toString(), this);
        sleep(4000);    //在每个test之间预留4秒的缓冲
    }
    @After
    public void teardown(){
        unAllRegIdlingResource();
    }

    /**
     * 等待 1 秒后截屏 , 确保元素完全展示
     * 该功能也用在 {@link AdViewInteraction check(ViewInteraction, ViewAssertion) ;}
     * and {@link AdViewInteraction perform(ViewInteraction, ViewAction...)}
     * @return 截屏图片的绝对路径
     */
    public String takeScreenshot() {
        sleep(1000);
        String fPath = DoIt.takeScreenshot(uiDevice, testWatcherAdvance.getTestName());
        Log.i(BTAG, "takeScreenshot: 截图成功 ; 保存路径 : " + fPath);
        return fPath;
    }

    /**
     * 截图并将路径保存至list
     * @param secondName
     */
    public void compareTakeScreenshot(String secondName){
        this.compareImageNames.add(DoIt.takeScreenshot(uiDevice ,secondName));
    }

    public List<String> getCompareImageNames() {
        return compareImageNames;
    }


//    /* 一些常用的操作 ( 公共的 )*/
//    protected void back_to_main(){
//        while (Wish.isVisible(GlobalView.back_up)) {
//            given(GlobalView.back_up).perform(click());
//        }
//    }
//
//    protected void back_loop(int times) {
//        for (int i=0;i<times;i++) {
//            given(GlobalView.back_up).perform(click());
//        }
//    }
//
//    protected void back_till(Matcher<View> matcher) {
//        do {
//            given(GlobalView.back_up).perform(click());
//        } while (Wish.isVisible(matcher));
//    }
    protected void updateData(String name , Object value){
        Map<String, Object> uData = testWatcherAdvance.getUpdateData();
        if (uData == null) {
            uData = new HashMap<>();
        }
        uData.put(name, value);
        testWatcherAdvance.setUpdateData(uData);
    }

    /**
     * 设置运行时需要的参数
     * @param key
     * @param value
     */
    public void runTimeData(String key , Object value) {
        Map<String, Object> runTimeData = getInstantiateRunTimeData();
        runTimeData.put(key, value);
    }
    public void runTimeData(Object... values){
        int len = values.length;
        if(len < 2 || len % 2 != 0) return;

        Map<String, Object> runTimeData = getInstantiateRunTimeData();
        for (int i=0; i<len; i+=2) {
            runTimeData.put((String) values[i], values[i+1]);
        }
    }
//    @SafeVarargs
//    public final void runTimeData(Kvp<String, Object>... kvps){
//        Map<String, Object> runTimeData = getInstantiateRunTimeData();
//        for (Kvp<String,Object> kvp : kvps) {
//            runTimeData.put(kvp.getKey(), kvp.getValue());
//        }
//    }
    private Map<String, Object> getInstantiateRunTimeData(){
        Map<String, Object> runTimeData = testWatcherAdvance.getRunTimeData();
        if (runTimeData == null) {
            runTimeData = new HashMap<>();
            // 如果get过来的值为null 则表示没有引用 , so 需要调用 set方法
            testWatcherAdvance.setRunTimeData(runTimeData);
        }
        return runTimeData;
    }

//    /**
//     * 获取用户的实时密码
//     * @param userName
//     * @param password
//     * @return
//     */
//    protected String theCurrent(String userName, String password) {
//        switch (password) {
//            case CURRENT_LOGIN_PWD:
//                password = getLoginPassword(userName);
//                this.updateData(OL_PIN,password);
//                break;
//            case CURRENT_TRADE_PWD:
//                password = getTradePassword(userName);
//                this.updateData(OT_PIN,password);
//                break;
//        }
//        return password;
//    }
//
//    /**
//     * 获取用户的登录密码 和 交易密码
//     *      未开户用户请勿使用该方法 , 只需要单独密码的情况请使用{@link #theCurrent(String, String)}
//     * @param userName
//     * @return
//     */
//    protected List theCurrent(String userName) {
//        List allPassword = UserStore.getAllPassword(userName);
//        this.updateData(OL_PIN,allPassword.get(0));
//        this.updateData(OT_PIN,allPassword.get(1));
//        return allPassword;
//    }
//
//    /**
//     * 实时获取密码  , 并替换
//     *      好处, 不用手动更新数据 , 也避免了参数名称设置的麻烦
//     * @param userName
//     * @param password
//     */
//    protected void theCurrent(String userName , Password password){
//        switch (password.getPassword()) {
//            case CURRENT_LOGIN_PWD:
//                password.setPassword(getLoginPassword(userName));
//                break;
//            case CURRENT_TRADE_PWD:
//                password.setPassword(getTradePassword(userName));
//                break;
//        }
//    }
//    protected void theCurrent(String userName , Password loginPwd , Password tradePwd){
//        List allPassword = UserStore.getAllPassword(userName);
//        loginPwd.setPassword((String) allPassword.get(0));
//        tradePwd.setPassword((String) allPassword.get(1));
//    }
//
//    /**
//     * 获得一个随机密码
//     * @param password
//     * @return
//     */
//    protected String theRandom(String password) {
//        int len = Integer.valueOf(God.getNumberFromString(password));
//
//        if (password.startsWith(UserStore.RANDOM_LOGIN_PWD)) {
//            password = getRandomLoginPwd(len);
//            this.updateData(NL_PIN,password);
//            return password;
//        }
//        if (password.startsWith(UserStore.RANDOM_TRADE_PWD)) {
//            password = getRandomTradePwd(len);
//            this.updateData(NT_PIN,password);
//            return password;
//        }
//        return password;
//    }

//    /**
//     * 获取一个随机密码
//     *      使用了封装类型, 这样可直接修改
//     * @param pwd
//     */
//    protected void theRandom(Password pwd){
//        String password = pwd.getPassword();
//        int len = Integer.valueOf(getIntervalString(password ,P_START ,P_STOP));
//
//        if (password.startsWith(UserStore.RANDOM_LOGIN_PWD)) {
//            pwd.setPassword(getRandomLoginPwd(len));
//            return;
//        }
//        if (password.startsWith(UserStore.RANDOM_TRADE_PWD)) {
//            pwd.setPassword(getRandomTradePwd(len));
//            return;
//        }
//    }
}
