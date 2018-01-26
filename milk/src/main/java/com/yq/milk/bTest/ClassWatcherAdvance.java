package com.yq.milk.bTest;

import android.util.Log;

import com.yq.milk.beans.report.CaseBean;
import com.yq.milk.beans.report.ClassBean;
import com.yq.milk.beans.report.SuiteBean;
import com.yq.milk.constant.Env;
import com.yq.milk.constant.enumerate.TestResult;
import com.yq.milk.utils.God;
import com.yq.milk.utils.Registor;

import org.junit.rules.TestName;
import org.junit.runner.Description;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import static com.yq.milk.constant.enumerate.TestResult.FAILED;
import static com.yq.milk.constant.enumerate.TestResult.SKIPPED;
import static com.yq.milk.constant.enumerate.TestResult.SUCCEEDED;
import static com.yq.milk.constant.enumerate.TestResult.UNCALIBRATED;

/**
 * Created by king on 2016/9/20.
 * 用于创建class 级的测试报告
 */

public class ClassWatcherAdvance extends TestName {
    private final String TAG = "jcd_ClassWatcher";

//    private TestReport.TestClass testClass ;
    private ClassBean testClass ;
    private String testClassName;
    private long startTime;
    private long stopTime;


    /**
     * Invoked when a test is about to start
     */
    protected void starting(Description description) {
        this.startTime = new Date().getTime();
        this.testClassName = description.getClassName();
        Log.i(TAG, "starting: ...."+ testClassName + " at "+ God.getDateFormat(startTime));

        testClass = new ClassBean();
        testClass.setTestClassName(this.testClassName);
        testClass.setTestCases(new ArrayList<CaseBean>());

//        testClass = CreateReport.getTestReport().createTestClass();
//        testClass.setTestClassName(this.testClassName);
//        testClass.setTestCases(new ArrayList<TestReport.TestClass.TestCase>());
    }

    /**
     * Invoked when a test method finishes (whether passing or failing)
     */
    protected void finished(Description description) {
        this.stopTime = new Date().getTime();
        Log.i(TAG, "finished: ...."+ testClassName + " at "+God.getDateFormat(stopTime));

        testClass.setSpendTime(stopTime - startTime);
        SuiteBean testSuite = (SuiteBean) Registor.peekReg(Env.suiteRegisterKey);
        assert testSuite != null;
        testClass.setStatus_count(create_status_count(testClass));
        testSuite.addTestClasses(testClass);

//        CreateReport.getTestReport().getTestClasses().add(testClass);
    }


    public ClassBean getTestClass() {
        return testClass;
    }
    private Map<TestResult,Integer> create_status_count(ClassBean classBean){
        Map<TestResult,Integer> count = new HashMap<TestResult,Integer>();
        int p=0 , s=0 , f=0 ,u=0;
        for(CaseBean caseBean : classBean.getTestCases()){
            switch (caseBean.getTestResult()){
                case SUCCEEDED:
                    p++;
                    break;
                case SKIPPED:
                    s++;
                    break;
                case FAILED:
                    f++;
                    break;
                case UNCALIBRATED:
                    u++;
                    break;
            }
        }
        count.put(SUCCEEDED ,p);
        count.put(SKIPPED ,s);
        count.put(FAILED ,f);
        count.put(UNCALIBRATED, u);
        return count;
    }
}
