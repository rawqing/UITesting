package com.yq.milk.custom.parameters.provider;

import com.alibaba.fastjson.JSON;
import com.trubuzz.trubuzz.feature.custom.parameters.GatherParameter;

import org.junit.runners.model.FrameworkMethod;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import junitparams.custom.ParametersProvider;

import static com.trubuzz.trubuzz.constant.Env.p_s;

/**
 * Created by king on 2017/7/3.
 */

public class GatherParameterProvider implements ParametersProvider<GatherParameter> {
    private GatherParameter parametersAnnotation;

    @Override
    public void initialize(GatherParameter parametersAnnotation, FrameworkMethod frameworkMethod) {
        this.parametersAnnotation = parametersAnnotation;
    }

    @Override
    public Object[] getParameters() {
        String[] values = parametersAnnotation.value();
        List res = new ArrayList();
        for(String value : values) {
            if (value.startsWith("{")) {
                String val;
                if(value.endsWith("}")) val = value;
                else val = formatString(value, "}" ,p_s);
                HashMap map = JSON.parseObject(val, HashMap.class);
                res.add(map);
            }else
            if (value.startsWith("[")) {
                String val;
                if(value.endsWith("]")) val = value;
                else val = formatString(value, "]" ,p_s);
                ArrayList list = JSON.parseObject(val, ArrayList.class);
                res.add(list);
            }else {
                res.add(value);
            }
        }
        // 若value()的返回结果不是数组
        if (values.length == 1) {
            return new Object[]{res.get(0)};
        }
        return new List[]{res};
    }

    private String formatString(String s, String end , String p_s) {
        int i = s.indexOf(p_s);
        if (i <= 0) {
            // 若在结尾标识出现后后面仍有字符 , 则将多余字符忽略
            return s.substring(0, s.indexOf(end) + 1);
        }
        // 验证参数标志前为结尾标识 否则忽略结尾标识后的字符
        String c = String.valueOf(s.charAt(i-1));
        if (end.equals(c)) {
            String[] values = s.split(p_s);
            // 若参数为空 , 则将多余字符忽略
            if (values.length < 2) {
                return s.substring(0, s.indexOf(end) + 1);
            }
            Object[] ss = values[1].split(",");
            return String.format(values[0], ss);
        }
        return s.substring(0, s.indexOf(end) + 1);
    }
}
