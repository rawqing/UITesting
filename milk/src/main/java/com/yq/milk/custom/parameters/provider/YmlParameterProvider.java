package com.yq.milk.custom.parameters.provider;

import com.trubuzz.trubuzz.constant.Env;
import com.trubuzz.trubuzz.feature.custom.parameters.YamlFileName;
import com.trubuzz.trubuzz.feature.custom.parameters.YmlParameter;
import com.trubuzz.trubuzz.utils.MReflect;

import org.junit.runners.model.FrameworkMethod;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import junitparams.custom.ParametersProvider;

import static com.trubuzz.trubuzz.utils.DoIt.castObject;
import static com.trubuzz.trubuzz.utils.DoIt.list2array;
import static com.trubuzz.trubuzz.utils.FileRw.readYamlFile;

/**
 * Created by king on 2017/7/14.
 */

public class YmlParameterProvider implements ParametersProvider<YmlParameter> {
    private YmlParameter ymlParameter;
    private Class<?>[] parameterTypes;
    private Class<?> testClass;
    private String testMethodName;
    private String testClassName;
    private final String class_key = "class";
    private final String method_key = "method";

    @Override
    public void initialize(YmlParameter ymlParameter, FrameworkMethod frameworkMethod) {
        this.ymlParameter = ymlParameter;
        this.parameterTypes = frameworkMethod.getMethod().getParameterTypes();
        this.testMethodName = frameworkMethod.getName();

        Class<?> declaringClass = frameworkMethod.getDeclaringClass();
        this.testClass = declaringClass;
        this.testClassName = declaringClass.getSimpleName();

    }

    @Override
    public Object[] getParameters() {
        List params = getMethodParams();
        if (params == null) {
            throw new RuntimeException("Empty parameters .");
        }
        List res = new ArrayList();
        for (Object po : params) {
            List data = null;
            // 将每行数据封装成 List
            if (po instanceof List) {
                data = (List) po;
            } else {
                data = new ArrayList();
                data.add(po);
            }

            res.add(createParams(data ,parameterTypes));
        }
        return list2array(res);
    }

    /**
     * 创建参数
     * @param datas
     * @param types
     * @return
     */
    public Object[] createParams(List datas , Class[] types){
        int dataSize = datas.size();
        int typeLen = types.length;
        // 判断参数个数与参数类型个数是否一致
        if (dataSize != typeLen) {
            throw new RuntimeException(String.format("参数个数(%s)与参数类型个数(%s)不匹配",
                    dataSize, typeLen));
        }
        Object[] o = new Object[typeLen];
        for(int i=0;i<typeLen; i++) {
            Object data = datas.get(i);
            o[i] = castObject(types[i], data);
        }
        return o;
    }
    /**
     * 获取当前方法在 yml 文件中定义的参数
     * @return
     */
    private List getMethodParams(){
        Object yamlObject = this.getYamlObject(ymlParameter.value());
        if (yamlObject == null) {
            throw new RuntimeException("Empty parameters .");
        }
        List datas = (List) yamlObject;
        for (Object o : datas) {
            Map map = (Map)o;
            if (testMethodName.equals(map.get("name"))) {
                return (List) map.get("data");
            }
        }
        return null;
    }
    /**
     * 捕获 yml 文档中当前 test class 的所有 method 的数据
     *      每个文档必须包含 "class" , "method" 这两个key
     * @param ymlFileName
     * @return method 这个 key 的value
     *      一般为 ArrayList
     */
    private Object getYamlObject(String ymlFileName){
        if ("".equals(ymlFileName)) {
            ymlFileName = (String) MReflect.getFieldObject(this.testClass, null, YamlFileName.class);
        }
        String dir = Env.condition.dir();
        String path = Env.test_data_root_dir + File.separator + dir + File.separator + ymlFileName;
        List list = readYamlFile(path);
        for (Object obj : list) {
            Map map = (Map) obj;
            Object aClass = map.get(class_key);
            if (testClassName.equals(aClass)) {
                return map.get(method_key);
            }
        }
        return null;
    }

}
