package com.jlc.app.milk_mini.bTest.rules;

import android.app.Activity;
import android.app.Instrumentation;
import android.app.KeyguardManager;
import android.app.KeyguardManager.KeyguardLock;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.IBinder;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.support.test.InstrumentationRegistry;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitor;
import android.support.test.runner.lifecycle.ActivityLifecycleMonitorRegistry;
import android.support.test.runner.lifecycle.Stage;
import android.util.Log;
import android.view.WindowManager;

import java.lang.reflect.Method;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.Callable;
import java.util.concurrent.atomic.AtomicReference;

import static com.jlc.app.milk_mini.constant.Conf.TAG;

/**
 * Make sure this gets added to the manifest of the application under test (typically a manifest in the debug variant).
 *
 <code>
 <uses-permission android:name="android.permission.SET_ANIMATION_SCALE" />
 <uses-permission android:name="android.permission.DISABLE_KEYGUARD"/>
 <uses-permission android:name="android.permission.WAKE_LOCK"/>
 </code>
 *
 * @param <T>
 */
public class ActivityLife<T>{
    private static final String ANIMATION_PERMISSION = "android.permission.SET_ANIMATION_SCALE";
    private static final float ANIMATION_DISABLED = 0.0f;
    private static final float ANIMATION_DEFAULT = 1.0f;


    private void disableAllAnimations() {
        if (getAnimationPermissionStatus() == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(ANIMATION_DISABLED);
        } else {
            Log.e(TAG, "Not granted permission to change animation scale.");
        }
    }

    private void enableAllAnimations() {
        if (getAnimationPermissionStatus() == PackageManager.PERMISSION_GRANTED) {
            setSystemAnimationsScale(ANIMATION_DEFAULT);
        } else {
            Log.e(TAG, "Not granted permission to change animation scale.");
        }
    }

    private int getAnimationPermissionStatus() {
        Context context = InstrumentationRegistry.getTargetContext();
        return context.checkCallingOrSelfPermission(ANIMATION_PERMISSION);
    }

    // https://code.google.com/p/android-test-kit/wiki/DisablingAnimations
    private void setSystemAnimationsScale(float animationScale) {
        try {
            Class<?> windowManagerStubClazz = Class.forName("android.view.IWindowManager$Stub");
            Method asInterface = windowManagerStubClazz.getDeclaredMethod("asInterface", IBinder.class);
            Class<?> serviceManagerClazz = Class.forName("android.os.ServiceManager");
            Method getService = serviceManagerClazz.getDeclaredMethod("getService", String.class);
            Class<?> windowManagerClazz = Class.forName("android.view.IWindowManager");
            Method setAnimationScales = windowManagerClazz.getDeclaredMethod("setAnimationScales", float[].class);
            Method getAnimationScales = windowManagerClazz.getDeclaredMethod("getAnimationScales");

            IBinder windowManagerBinder = (IBinder) getService.invoke(null, "window");
            Object windowManagerObj = asInterface.invoke(null, windowManagerBinder);
            float[] currentScales = (float[]) getAnimationScales.invoke(windowManagerObj);
            for (int i = 0; i < currentScales.length; i++) {
                currentScales[i] = animationScale;
            }
            setAnimationScales.invoke(windowManagerObj, new Object[]{currentScales});
        } catch (Exception ex) {
            Log.e(TAG, "Could not use reflection to change animation scale to: " + animationScale, ex);
        }
    }

    // See https://code.google.com/p/android-test-kit/issues/detail?id=66
    public void closeAllActivities() {
        try {
            Instrumentation instrumentation = InstrumentationRegistry.getInstrumentation();
            closeAllActivities(instrumentation);
        } catch (Exception ex) {
            Log.e(TAG, "Could not use close all activities", ex);
        }
    }

    private void closeAllActivities(Instrumentation instrumentation) throws Exception {
        final int NUMBER_OF_RETRIES = 100;
        int i = 0;
        while (closeActivity(instrumentation)) {
            if (i++ > NUMBER_OF_RETRIES) {
                throw new AssertionError("Limit of retries excesses");
            }
            Thread.sleep(200);
        }
    }

    private boolean closeActivity(Instrumentation instrumentation) throws Exception {
        final Boolean activityClosed = callOnMainSync(instrumentation, new Callable<Boolean>() {
            @Override
            public Boolean call() throws Exception {
                final Set<Activity> activities = getActivitiesInStages(Stage.RESUMED,
                        Stage.STARTED, Stage.PAUSED, Stage.STOPPED, Stage.CREATED);
                activities.removeAll(getActivitiesInStages(Stage.DESTROYED));
                if (activities.size() > 0) {
                    final Activity activity = activities.iterator().next();
                    activity.finish();
                    return true;
                } else {
                    return false;
                }
            }
        });
        if (activityClosed) {
            instrumentation.waitForIdleSync();
        }
        return activityClosed;
    }

    private <X> X callOnMainSync(Instrumentation instrumentation, final Callable<X> callable) throws Exception {
        final AtomicReference<X> retAtomic = new AtomicReference<>();
        final AtomicReference<Throwable> exceptionAtomic = new AtomicReference<>();
        instrumentation.runOnMainSync(new Runnable() {
            @Override
            public void run() {
                try {
                    retAtomic.set(callable.call());
                } catch (Throwable e) {
                    exceptionAtomic.set(e);
                }
            }
        });
        final Throwable exception = exceptionAtomic.get();
        if (exception != null) {
            exception.printStackTrace();
        }
        return retAtomic.get();
    }

    public static Set<Activity> getActivitiesInStages(Stage... stages) {
        final Set<Activity> activities = new HashSet<>();
        final ActivityLifecycleMonitor instance = ActivityLifecycleMonitorRegistry.getInstance();
        for (Stage stage : stages) {
            final Collection<Activity> activitiesInStage = instance.getActivitiesInStage(stage);
            if (activitiesInStage != null) {
                activities.addAll(activitiesInStage);
            }
        }
        return activities;
    }
}