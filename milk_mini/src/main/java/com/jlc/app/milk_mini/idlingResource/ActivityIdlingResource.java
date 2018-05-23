package com.jlc.app.milk_mini.idlingResource;

import android.content.Context;
import android.support.test.espresso.IdlingResource;
import android.util.Log;

import com.jlc.app.milk_mini.utils.Judge;


/**
 * Created by king on 2016/9/2.
 * 用于等待给定info的activity处于栈顶
 */
public class ActivityIdlingResource implements IdlingResource {
    private final String TAG = "jcd_" + this.getClass().getSimpleName();

    private ResourceCallback resourceCallback;
    private boolean isIdle;
    private String currentActivityName ;
    private Context context;
    private boolean waitingAway;


    /**
     *
     * @param currentActivityName
     * @param context
     * @param waitingAway 如果为true,则目的为等待指定的activity消失, 如果为false, 则为等待指定activity出现
     */
    public ActivityIdlingResource(String currentActivityName , Context context , boolean waitingAway){
        this.currentActivityName = currentActivityName;
        this.context = context;
        this.waitingAway = waitingAway;
    }

    @Override
    public String getName() {
        return this.getClass().getName()+currentActivityName;
    }

    @Override
    public boolean isIdleNow() {
        if (isIdle) return true;

        isIdle = Judge.isTopActivity(currentActivityName,context);
        Log.i(TAG, (Boolean.toString(isIdle)));
        if(!waitingAway){
            isIdle = ! isIdle;
        }   //如果为等待出现,则对当前isIdle的值取反即可

        if (isIdle) {
            resourceCallback.onTransitionToIdle();
        }
        return isIdle;
    }

    @Override
    public void registerIdleTransitionCallback(ResourceCallback callback) {
        this.resourceCallback = callback;
    }

}
