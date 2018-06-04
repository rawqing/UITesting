package com.yq.milk.shell;

import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewAction;
import android.support.test.espresso.ViewAssertion;
import android.support.test.espresso.ViewInteraction;
import android.support.test.espresso.action.ViewActions;
import android.util.Log;
import android.view.View;

import com.yq.milk.bTest.BaseTest;
import com.yq.milk.elements.Element;
import com.yq.milk.utils.DoIt;
import com.yq.milk.utils.Registor;

import org.hamcrest.Matcher;

import static android.support.test.espresso.action.ViewActions.click;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static com.yq.milk.shell.Park.getViewInteraction;

/**
 * Created by king on 16/10/24.
 */

public class AdViewInteraction {
    private static final String TAG = "jcd_AdViewInteraction";
    private static String key = AdViewInteraction.class.toString();
    private Element viewElement ;
    private final ViewInteraction viewInteraction;


    public AdViewInteraction(Element viewElement){
        this.viewElement = viewElement;
        this.viewInteraction = getViewInteraction(viewElement);
    }

    public AdViewInteraction(ViewInteraction viewInteraction) {
        this.viewInteraction = viewInteraction;
    }

    /**
     * 改良后的perform , 加入等待与错误截图
     * @param times
     * @param viewActions
     * @return
     */
    public AdViewInteraction perform(final int times ,final ViewAction... viewActions) {
        for(int i = 1; i<times ; i++){

            Log.i(TAG, String.format("perform: 开始第 %s 次匹配 ' %s ' .", i,
                    viewElement == null ? viewInteraction.toString() : viewElement.toString()));
            if(canPerform(this.viewInteraction, viewActions)){
                DoIt.delFile((String) Registor.unReg(key));
                return this;
            }else if(i==2 ){
                //执行截图并保存文件名
                Registor.reg( key , ((BaseTest)Registor.peekReg(BaseTest.class.toString())).takeScreenshot());
            }
            Log.w(TAG, String.format("perform: 第 %s 次未匹配到元素 ' %s ' .",i ,
                    viewElement == null ? viewInteraction.toString() : viewElement.toString()));
        }
        this.viewInteraction.perform(viewActions);
        DoIt.delFile((String)Registor.unReg(key));
        return this;
    }
    public AdViewInteraction perform(final ViewAction... viewActions) {
        return this.perform(5 ,viewActions);
    }
    public AdViewInteraction perform(boolean isTry ,final ViewAction... viewActions) {
        if(isTry){
            try {
                return perform(viewActions);
            }catch (Throwable e ){
                return this;
            }
        }else{
            return perform(viewActions);
        }
    }

    /**
     * 点击
     * @return
     */
    public  AdViewInteraction click(){
        return this.perform(ViewActions.click());
    }

    /**
     * 注入字符串
     * @param text
     * @return
     */
    public  AdViewInteraction replaceText(String text){
        return this.perform(ViewActions.replaceText(text));
    }

    /**
     * 使用键盘输入字符串
     * @param text
     * @return
     */
    public  AdViewInteraction typeText(String text){
        return this.perform(ViewActions.typeText(text));
    }
    /**
     * 判断操作是否成功
     * @param v
     * @param viewActions
     * @return
     */
    public static boolean canPerform(final ViewInteraction v , final ViewAction... viewActions ) {
        try {
            v.perform(viewActions);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 改良后的check , 加入等待与错误截图
     * @param times
     * @param viewAssert
     * @return
     */
    public AdViewInteraction check(final int times ,final ViewAssertion viewAssert){
        for(int i=1; i<times ; i++){
            Log.i(TAG, String.format("check: 开始第 %s 次匹配 ' %s ' .",i,
                    viewElement == null ? viewInteraction.toString() : viewElement.toString()));
            if(checkRight(viewAssert)){
                DoIt.delFile((String)Registor.unReg(key));
                return this;
            }else if(i==2 ){
                //执行截图并保存文件名
                Registor.reg( key , ((BaseTest)Registor.peekReg(BaseTest.class.toString())).takeScreenshot());
            }
            Log.w(TAG, String.format("check: 第 %s 次未匹配到元素 ' %s ' .",i ,
                    viewElement == null ? viewInteraction.toString() : viewElement.toString()));
        }
        this.viewInteraction.check(viewAssert);
        DoIt.delFile((String)Registor.unReg(key));
        return this;
    }
    /**
     * 默认匹配 5 次
     * @param viewAssert
     * @return
     */
    public AdViewInteraction check(final ViewAssertion viewAssert){
        return check(5 , viewAssert);
    }

    public AdViewInteraction check(final Matcher<View> matcher) {
        return check(matches(matcher));
    }
    /**
     * 尝试check
     * @param isTry
     * @param viewAssert
     * @return
     */
    public AdViewInteraction check(boolean isTry ,final ViewAssertion viewAssert){
        if(isTry){
            try {
                return check(viewAssert);
            }catch (Throwable e ){
                return this;
            }
        }else
            return check(viewAssert);
    }

    /**
     * 判断断言是否成功
     * @param viewAssert
     * @return
     */
    public boolean checkRight(final ViewAssertion viewAssert){
        try {
            this.viewInteraction.check(viewAssert);
            return true;
        } catch (NoMatchingViewException e) {
            Log.e(TAG, "checkRight: check "+ viewAssert.toString() +" not match ", e);
            e.printStackTrace();
            return false;
        } catch (Error error){
            Log.e(TAG, "checkRight: check " + viewAssert.toString() + " assertion failed", error );
            return false;
        }
    }

    /**
     * 暂停time毫秒
     * @param time
     * @return
     */
    public AdViewInteraction pause(long time){
        try {
            Thread.sleep(time);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        return this;
    }

//    /**
//     * @deprecated use {@link ViewsFinder#getViews(Matcher)}
//     * 由于过度强依赖 , 过度反射, 故决定弃用.
//     * 匹配到多个View时使用
//     * 强依赖于{@link ViewInteraction#viewFinder};{@link ViewInteraction#uiController};{@link ViewInteraction#failureHandler}
//     *          {@link ViewInteraction#mainThreadExecutor};{@link ViewInteraction#viewMatcher} .
//     *          {@link android.support.test.espresso.base.ViewFinderImpl#viewMatcher};
//     *          {@link android.support.test.espresso.base.ViewFinderImpl#rootViewProvider}
//     * ViewInteraction.viewMatcher == ViewFinderImpl.viewMatcher
//     * @return
//     */
//    public List<AdViewInteraction> getInteractionList(){
//        List<AdViewInteraction> adViewInteractions = null;
//        try {
//            ViewFinder viewFinder = null;
//            UiController uiController = null;
//            FailureHandler failureHandler = null;
//            Matcher<View> baseViewMatcher = null;
//            Executor mainThreadExecutor = null;
//            Field[] fields = getDecFields(viewInteraction);
//            for(Field field : fields){
//                switch (field.getName()){
//                    case "viewFinder" :
//                        viewFinder = (ViewFinder) field.get(viewInteraction);
//                        break;
//                    case "uiController" :
//                        uiController = (UiController) field.get(viewInteraction);
//                        break;
//                    case "failureHandler" :
//                        failureHandler = (FailureHandler) field.get(viewInteraction);
//                        break;
//                    case "viewMatcher" :
//                        baseViewMatcher = (Matcher<View>) field.get(viewInteraction);
//                        break;
//                    case "mainThreadExecutor" :
//                        mainThreadExecutor = (Executor) field.get(viewInteraction);
//                        break;
//                }
//            }
//            Provider<View> rootViewProvider = (Provider<View>) getFieldObject("rootViewProvider", viewFinder);
//            ViewTracer viewTracer = new ViewTracer(baseViewMatcher, rootViewProvider);
//
//            ViewHandle viewHandle = new ViewHandle(uiController, mainThreadExecutor, failureHandler, baseViewMatcher, viewTracer);
//
//            List<View> views = viewHandle.getTargetViews();
//            adViewInteractions = new ArrayList<>();
//            for(View view : views){
//                adViewInteractions.add(new AdViewInteraction(new ActivityElement().setMatchers(withView(view))));
//            }
//        }catch (Exception e ){
//            e.printStackTrace();
//        }
//        return adViewInteractions;
//    }
}
