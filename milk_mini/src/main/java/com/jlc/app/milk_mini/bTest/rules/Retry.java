package com.jlc.app.milk_mini.bTest.rules;

import android.util.Log;

import com.jlc.app.milk_mini.exception.AcceptableException;

import org.junit.rules.TestRule;
import org.junit.runner.Description;
import org.junit.runners.model.Statement;

import static com.jlc.app.milk_mini.constant.Conf.TAG;

public class Retry implements TestRule {
    private int retryCount;
    public Retry(int retryCount) {
        this.retryCount = retryCount;
    }
    public Statement apply(Statement base, Description description) {
        return statement(base, description);
    }
    private Statement statement(final Statement base,
                                final Description description) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Throwable caughtThrowable = null;
                //implement retry logic here
                for (int i = 0; i <retryCount; i++) {
                    try {
                        if (i>0){
                            ActivityLife rt = new ActivityLife();
                            rt.closeAllActivities();
                        }
                        base.evaluate();
                        return;
                    } catch (Throwable t) {
                        if (t instanceof AcceptableException) {
                            Log.w(TAG, description.getDisplayName()+ ": ", t);
                            return;
                        }
                        caughtThrowable = t;
                        Log.e(TAG, description.getDisplayName()
                                +": run " + (i + 1) +" failed !" ,t);
                    }
                }
                Log.e(TAG, description.getDisplayName()
                        +": giving up after " + retryCount +" failures !" ,caughtThrowable);
                if (caughtThrowable != null) {
                    throw caughtThrowable;
                }
            }
        };
    }
}