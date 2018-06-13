package com.jlc.app.milk_mini.bTest;

import android.support.test.runner.AndroidJUnit4;

import com.jlc.app.milk_mini.bTest.rules.Retry;

import org.junit.Rule;
import org.junit.rules.ErrorCollector;
import org.junit.rules.RuleChain;
import org.junit.rules.TestRule;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RetryTest {

    private ErrorCollector collector = new ErrorCollector();
    private Retry retry = new Retry(3);
    // 失败重试规则定义 ( 定义为3次 )
    @Rule
    public TestRule chain= RuleChain
            . outerRule(collector)
            . around(retry);
}
