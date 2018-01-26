package com.yq.milk.utils;

import android.content.Context;
import android.content.res.Resources;
import android.support.test.espresso.NoMatchingViewException;
import android.support.test.espresso.ViewInteraction;
import android.support.test.rule.ActivityTestRule;
import android.util.Log;

import static android.support.test.InstrumentationRegistry.getTargetContext;
import static android.support.test.espresso.assertion.ViewAssertions.matches;
import static android.support.test.espresso.matcher.RootMatchers.withDecorView;
import static android.support.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.core.IsNot.not;

/**
 * Created by king on 2016/6/29.
 */
public class Find {

    private static Context context = getTargetContext();
    private static Resources resources = context.getResources();
    private static final String defaultPackageName = "com.trubuzz.trubuzz";


    /**
     * 通过完整的resourcesId 返回id值
     * @param resourcesId  uiAutomator 查询得到的resourcesId
     * @return
     */
    public static int byId(String resourcesId){
        return byId(resourcesId, resources);
    }
    public static int byId(String resourcesId , Resources resources){
        String packageName = resourcesId.split(":")[0];
        String sid = resourcesId.split("/")[1];
        return findId(packageName,sid ,resources);
    }
    /**
     * 使用默认的包名 和 id名 查找id值
     * @param idName
     * @return
     */
    public static int byShortId(String idName){
        return findId(defaultPackageName,idName ,resources);
    }

    /**
     * 通过包名和id名 返回id值
     * @param packageName
     * @param idName
     * @return
     */
    private static int findId(String packageName , String idName , Resources resources){
        int id = resources.getIdentifier(idName ,"id",packageName);
        Log.d(idName , idName + " = "+id);
        return  id;
    }

    //判断指定的toast是否存在
    public static boolean isToast(ViewInteraction v , ActivityTestRule<?> a){
        try {
            v.inRoot(withDecorView(not(is(a.getActivity().getWindow().getDecorView())))).check(matches(isDisplayed()));
            Log.i("find toast", "ok");
            return true;
        }catch (NoMatchingViewException nsve){
            Log.w("NoMatchingView",nsve);
        }
        return false;
    }


}
