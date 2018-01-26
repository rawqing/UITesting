package com.yq.milk.beans.report;

import android.os.Bundle;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.yq.milk.constant.DeviceDesc;
import com.yq.milk.constant.Env;
import com.yq.milk.utils.DoIt;

/**
 * Created by king on 16/10/27.
 */

public class Report {
    private static final String TAG = "Report";
    private static Report report;
    private String logcatPath;
    private String reportName;
    private long spendTime;
    private long testDate;
    private SuiteBean suiteBean;
    private DeviceDesc deviceDesc;

    public static synchronized Report getReport(){
        if(report == null) report = new Report();
        return report;
    }

    /**
     * 将测试结果写文件
     * {//@link com.trubuzz.trubuzz.feature.AdRunner#finish(int, Bundle)} ) 中进行调用}
     */
    public String testOutputReport(){
        initDesc();
        Log.i(TAG, "testOutputReport: "+ report.toString());
        String str = JSON.toJSONString(report);
        return DoIt.writeFileData(str,"report");
    }

    /**
     * 初始化一些描述数据
     */
    private void initDesc(){
        this.reportName = Env.reportName;
        this.deviceDesc = DeviceDesc.getDeviceDesc();
        this.logcatPath = null;

    }

    private Report(){}
    public String getLogcatPath() {
        return logcatPath;
    }

    public void setLogcatPath(String logcatPath) {
        this.logcatPath = logcatPath;
    }

    public String getReportName() {
        return reportName;
    }

    public void setReportName(String reportName) {
        this.reportName = reportName;
    }

    public long getSpendTime() {
        return spendTime;
    }

    public void setSpendTime(long spendTime) {
        this.spendTime = spendTime;
    }

    public long getTestDate() {
        return testDate;
    }

    public void setTestDate(long testDate) {
        this.testDate = testDate;
    }

    public SuiteBean getSuiteBean() {
        return suiteBean;
    }

    public void setSuiteBean(SuiteBean suiteBean) {
        this.suiteBean = suiteBean;
    }

    public DeviceDesc getDeviceDesc() {
        return deviceDesc;
    }

    public void setDeviceDesc(DeviceDesc deviceDesc) {
        this.deviceDesc = deviceDesc;
    }
}
