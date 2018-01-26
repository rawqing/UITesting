package com.yq.milk.bTest;

import android.util.Log;


import com.yq.milk.annotation.FieldVar;
import com.yq.milk.annotation.Uncalibrated;
import com.yq.milk.annotation.Var;
import com.yq.milk.beans.report.CaseBean;
import com.yq.milk.beans.report.ClassBean;
import com.yq.milk.constant.enumerate.TestResult;
import com.yq.milk.custom.parameters.GatherParameter;
import com.yq.milk.custom.parameters.GenreParameter;
import com.yq.milk.custom.parameters.YmlParameter;
import com.yq.milk.shell.AdViewInteraction;
import com.yq.milk.utils.God;
import com.yq.milk.utils.Registor;

import org.junit.AssumptionViolatedException;
import org.junit.rules.TestName;
import org.junit.runner.Description;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import junitparams.JUnitParamsRunner;
import junitparams.Parameters;

import static com.yq.milk.utils.DoIt.notEmpty;


/**
 * Created by king on 2016/9/20.
 */

public class TestWatcherAdvance extends TestName {
    private final String TAG = "jcd_TestWatcher";

    private String testName;
    private TestResult result;
    private String message;
    private String errorImagePath;
    private ClassBean testClass;
    private BaseTest baseTest;
    private Map useData;
    private String localizedMessage;
    private String[] stackTraces;
    private long startTime;
    private long stopTime;
    private Map<String,Object> updateData;
    private Map<String,Object> runTimeData;

   // public TestWatcherAdvance(){}
    public TestWatcherAdvance(ClassBean testClass , BaseTest baseTest){
        this.testClass = testClass;
        this.baseTest = baseTest;
    }

//    public Statement apply(final Statement base, final Description description) {
//        return super.apply(base , description);
//    }

    /**
     * Invoked when a test is about to start
     */
    protected void starting(Description description) {
        this.startTime = new Date().getTime();
        this.testName = description.getMethodName();
        Log.i(TAG, "starting: ...."+ testName +" at "+ God.getDateFormat(this.startTime));
    }

    /**
     * Invoked when a test succeeds
     */
    protected void succeeded(Description description) {
        if(result == null)  this.result = TestResult.SUCCEEDED;
        Log.i(TAG, "succeeded: ");
    }

    /**
     * Invoked when a test fails
     */
    protected void failed(Throwable e, Description description) {
    /********** 这是将BaseTest 中After 搬过来的实现*********/
        //这里只做错误截图和捕获截图文件
        Object obj = Registor.unReg(AdViewInteraction.class.toString());
        if (obj instanceof String) {
            this.errorImagePath = (String) obj;
        } else {
            this.errorImagePath = baseTest.takeScreenshot();
        }

    /************** The End ***************/
        Log.e(TAG,"test failed : "+e.getMessage());
        if(result == null)    this.result = TestResult.FAILED;
        this.message = e.getMessage();                       //完整的错误信息
        this.localizedMessage = e.getLocalizedMessage();    //简短的错误信息
        StackTraceElement[] stackTraceElements = e.getStackTrace();        //错误堆栈信息
        stackTraces = new String[stackTraceElements.length];
        for(int i=0; i< stackTraceElements.length ; i++){
            stackTraces[i] = stackTraceElements[i].toString();
        }
    }

    /**
     * Invoked when a test is skipped due to a failed assumption.
     */
    @Deprecated
    protected void skipped(AssumptionViolatedException e, Description description) {
        super.skipped(e,description);
        if(result == null)  this.result = TestResult.SKIPPED;
        this.message = e.getMessage();
    }



    /**
     * Invoked when a test method finishes (whether passing or failing)
     * 测试完成后, 将该test case的直接结果写入报表中
     */
    protected void finished(Description description) {
        Registor.unRegAll(BaseTest.class.toString());

        this.setUseData(description);
        this.setUncalibrated(description);
        this.stopTime = new Date().getTime();
        Log.i(TAG, "finished: ...."+ testName + " at "+God.getDateFormat(stopTime));

        CaseBean testCase = new CaseBean();
        testCase.setCaseName(this.testName);
        testCase.setErrorMsg(this.message);
        testCase.setImageName(this.errorImagePath);
        testCase.setTestResult(this.result);
        testCase.setUseData(this.useData);
        testCase.setLocalizedMessage(this.localizedMessage);
        testCase.setStackTraces(this.stackTraces);
        testCase.setCompareImageNames(baseTest.getCompareImageNames());
        testCase.setSpendTime(stopTime - startTime);

        testClass.getTestCases().add(testCase);
    }

    /**
     * 设置需人工判断结果的 result 类型
     * 这里可能会擦出原有的 result类型 .
     * @param desc
     */
    private void setUncalibrated(Description desc){
        if (desc.getAnnotation(Uncalibrated.class) != null) {
            this.result = TestResult.UNCALIBRATED;
        }
    }
    /**
     * 设置 use data
     *      1. 获取使用成员属性作为的参数
     *      2. 获取使用参数注解得到的参数
     *          a. 更新执行过程中改变的值
     *      3. 获取使用的固定的参数 , 或者是新参数
     * @param desc
     * @return
     */
    private Map setUseData(Description desc){
        Map<String ,Object> mData = null;
        Map fData = getFieldData(this.baseTest , FieldVar.class);
        if(desc.getAnnotation(Parameters.class) != null ||
                desc.getAnnotation(GatherParameter.class) != null ||
                desc.getAnnotation(GenreParameter.class) != null ||
                desc.getAnnotation(YmlParameter.class) != null){
            Object[] objects = JUnitParamsRunner.getParams();
            // 如果在执行过程中改变了数据 , 将执行更新
            // 这里的updateData 的key 代表了params 的index
            mData =  putUseData(getParamsName(JUnitParamsRunner.getCurrentMethod()) ,objects);
            if(notEmpty(updateData)&&notEmpty(mData)){
                mData.putAll(updateData);
            }
        }
        if(mData != null && !mData.isEmpty()){
            if(this.useData != null){
                this.useData.putAll(mData);
            }else{
                this.useData = mData;
            }
        }
        if(fData != null && !fData.isEmpty()) {
            if (this.useData != null) {
                this.useData.putAll(fData);
            } else {
                this.useData = fData;
            }
        }
        if(runTimeData != null && !runTimeData.isEmpty()) {
            if (this.useData != null) {
                this.useData.putAll(runTimeData);
            } else {
                this.useData = runTimeData;
            }
        }
        return null;
    }

    /**
     * 获取使用属性定义的数据
     * 在属性上定义数据需使用特定的注解
     * @param baseTest 被获取的对象
     * @param annotation 特定的注解
     * @return key = field name ; value = value .
     */
    private Map getFieldData(BaseTest baseTest , Class annotation){
        Map<String,Object> data = new HashMap<>();
        Class clz = baseTest.getClass();
        Field[] fields = clz.getDeclaredFields();
        Field.setAccessible(fields , true);
        for(Field field : fields){
            try {
                // 这里只获取使用了@FieldVar注解过的的属性.
                if (field.isAnnotationPresent(annotation)) {
                    data.put(field.getName(), field.get(baseTest));
                }
            } catch (IllegalAccessException e) {
                e.printStackTrace();
            }
        }
        return data;
    }

    /**
     * 将使用{@link Parameters} and {@link GatherParameter} 获得的参数
     *      封装成 Map
     * @param name
     * @param data
     * @return
     */
    private Map<String , Object> putUseData(String[] name , Object[] data){
        if(name==null || data==null){
            Log.e(TAG, "putUseData: 未获取到形参名 或 形参值",new NullPointerException());
            return null;
        }
        Map<String , Object> useData = new HashMap<String , Object>();
        int dataLen = data.length;
        int nameLen = name.length;
        if(nameLen != dataLen) Log.w(TAG, "putUseData: 参数名和参数的数量不匹配 > "+"name = "+nameLen + " , data = "+dataLen );
        int len = dataLen > nameLen ? dataLen : nameLen;
        for(int i=0; i<len ;i++){
            useData.put(name[i] , data[i] == null ? null : data[i].toString());
        }
        return useData;
    }
    /**
     * get current params name
     * @param method
     * @return
     */
    private String[] getParamsName(Method method){
        if(method == null)  return null;

        Annotation[][] ass = method.getParameterAnnotations();
        String[] parameterNames = new String[ass.length];
        int i = 0;
        for(Annotation[] as : ass){
            for(Annotation a : as){
                if (a instanceof Var){
                    parameterNames[i++] = ((Var)a).value();
                }
            }
        }
        return parameterNames;
    }

    /**
     * 检查该方法是否是需要认为判断的
     * @param description
     * @param caseBean
     */
    public void checkUncalibrated(Description description ,CaseBean caseBean){
        Uncalibrated uncalibrated = description.getAnnotation(Uncalibrated.class);
        if(uncalibrated != null){
            this.result = TestResult.UNCALIBRATED;
            caseBean.setDesc(uncalibrated.value());
        }
    }

    public Map<String, Object> getUpdateData() {
        return updateData;
    }

    public Map<String, Object> getRunTimeData() {
        return runTimeData;
    }

    public String getTestName() {
        return testName;
    }

    public Map getUseData() {
        return useData;
    }

    public TestResult getResult() {
        return result;
    }

    public String getMessage() {
        return message;
    }

    public String getErrorImagePath() {
        return errorImagePath;
    }

    public void setErrorImagePath(String errorImagePath) {
        this.errorImagePath = errorImagePath;
    }

    public void setUseData(Map useData) {
        this.useData = useData;
    }

    public void setResult(TestResult result) {
        this.result = result;
    }

    /**
     * 测试时数据更新了 , 需要通知到改Watcher
     * @param updateData key == params的index , value == params的新值
     */
    public void setUpdateData(Map<String, Object> updateData) {
        this.updateData = updateData;
    }

    /**
     * 运行时新加入的参数 , 或者是使用的成员变量(当前测试方法所独有的)
     * @param runTimeData key : 必须保证不和其他参数同名 , 若同名则会覆盖原来参数的值
     *                          若需覆盖则建议使用{@link #setUpdateData(Map)}
     *                    value : 当前参数的值
     */
    public void setRunTimeData(Map<String, Object> runTimeData) {
        this.runTimeData = runTimeData;
    }
}
