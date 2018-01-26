package com.yq.milk.bTest;


import com.yq.milk.beans.report.ClassBean;
import com.yq.milk.beans.report.Report;
import com.yq.milk.beans.report.SuiteBean;
import com.yq.milk.constant.enumerate.TestResult;
import com.yq.milk.utils.Registor;


import org.junit.AfterClass;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yq.milk.constant.Env.suiteRegisterKey;
import static com.yq.milk.constant.enumerate.TestResult.FAILED;
import static com.yq.milk.constant.enumerate.TestResult.SKIPPED;
import static com.yq.milk.constant.enumerate.TestResult.SUCCEEDED;

/**
 * Created by king on 16/10/28.
 */

public class BaseSuite {
    private static String descSuite ;
    private static Class suiteClass;
    private static SuiteBean testSuite;
    private static long startTime ;

    /**
     * 由子类在@BeforeClass 中调用
     */
    protected static void baseSetup(){
        startTime = new Date().getTime();

        testSuite =  new SuiteBean();
        testSuite.setSuiteName(descSuite);
        testSuite.setSuiteClassName(suiteClass.toString());
        Registor.reg(suiteRegisterKey , testSuite);
    }
    @AfterClass
    public static void teardown(){
        Registor.unReg(suiteRegisterKey);
        testSuite.setSpendTime(new Date().getTime() - startTime);
        Report.getReport().getSuiteBean().addChildSuite(testSuite);

        descSuite = null;
        suiteClass = null;
        startTime = 0;
        testSuite = null;
    }

    protected static void init(String childDescSuite, Class childSuiteClass) {
        descSuite = childDescSuite;
        suiteClass = childSuiteClass;
    }

    /**
     * 用于统计该suite 中case的执行情况
     * 相当于将class中的status_count 汇总
     * use : 将结果set进{@link #testSuite} 即可
     * @param suite
     * @return
     */
    private static Map<TestResult, Integer> create_suite_status_count(SuiteBean suite){
        Map<TestResult,Integer> count = new HashMap<TestResult,Integer>();
        int p=0 , s=0 , f=0;
        for(ClassBean classBean : suite.getTestClasses()){
            Map<TestResult ,Integer> status = classBean.getStatus_count();
            p += status.get(SUCCEEDED);
            s += status.get(SKIPPED);
            f += status.get(FAILED);
        }

        count.put(SUCCEEDED ,p);
        count.put(SKIPPED ,s);
        count.put(FAILED ,f);
        return count;
    }
}
