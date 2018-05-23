package com.jlc.app.milk_mini.idlingResource;

import android.app.Activity;
import android.support.test.espresso.IdlingResource;
import android.webkit.WebChromeClient;
import android.webkit.WebView;

import static android.support.test.internal.util.Checks.checkNotNull;


/**
 * Created by king on 2016/9/8.
 */
public class WebViewIdlingResource extends WebChromeClient implements ActivityLifecycleIdlingResource<WebView> {
    private final String TAG = "jcd_WebIR";
    private static final int FINISHED = 100;

    private WebView webView;
    private IdlingResource.ResourceCallback callback;
    WebViewIdlingResource wid ;


    @Override
    public void inject(Activity activity, final WebView wv) {
        this.webView = checkNotNull(wv,String.format("Trying to instantiate a \'%s\' with a null WebView", getName()));
        // Save the original client if needed.
        wid = this;
        activity.runOnUiThread(new Runnable(){
            @Override
            public void run(){
                webView.setWebChromeClient(wid);
            }
        });
        //this.webView.setWebChromeClient(this);
    }

    @Override public void clear() {
        // Free up the reference to the webView
        webView = null;
    }



    @Override
    public void onProgressChanged(WebView view, int newProgress) {
        if (newProgress == FINISHED && view.getTitle() != null && callback != null) {
            callback.onTransitionToIdle();
        }
    }

    @Override
    public void onReceivedTitle(WebView view, String title) {
        if (webView.getProgress() == FINISHED && callback != null) {
            callback.onTransitionToIdle();
        }
    }

    @Override
    public String getName() {
        return "WebView idling resource";
    }

    @Override public boolean isIdleNow() {
        // The webView hasn't been injected yet, so we're idling
        if (webView == null) return true;
        return webView.getProgress() == FINISHED && webView.getTitle() != null;
    }

    @Override public void registerIdleTransitionCallback(IdlingResource.ResourceCallback resourceCallback) {
        this.callback = resourceCallback;
    }
}