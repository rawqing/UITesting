package com.yq.milk.beans.report;


import com.yq.milk.constant.enumerate.TestResult;

import java.util.List;
import java.util.Map;

/**
 * Created by king on 16/10/27.
 */

public class ClassBean {
    private String testClassName;
    private List<CaseBean> testCases;
    private Map<TestResult,Integer> status_count;
    private long spendTime;

    public ClassBean() {
    }

    public void setTestClassName(String testClassName) {
        this.testClassName = testClassName;
    }

    public void setTestCases(List<CaseBean> testCases) {
        this.testCases = testCases;
    }

    public void setSpendTime(long spendTime) {
        this.spendTime = spendTime;
    }

    public String getTestClassName() {
        return testClassName;
    }

    public List<CaseBean> getTestCases() {
        return testCases;
    }

    public long getSpendTime() {
        return spendTime;
    }

    public Map<TestResult, Integer> getStatus_count() {
        return status_count;
    }

    public void setStatus_count(Map<TestResult, Integer> status_count) {
        this.status_count = status_count;
    }
}
