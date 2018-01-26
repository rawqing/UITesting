package com.yq.milk.custom.parameters.provider;


import com.yq.milk.custom.parameters.GenreParameter;

import org.junit.runners.model.FrameworkMethod;

import java.util.ArrayList;
import java.util.List;

import junitparams.custom.ParametersProvider;

import static com.yq.milk.utils.DoIt.castObject;
import static com.yq.milk.utils.DoIt.list2array;


/**
 * Created by king on 2017/7/10.
 */

public class GenreParameterProvider implements ParametersProvider<GenreParameter> {
    private GenreParameter parametersAnnotation;
    private Class<?>[] parameterTypes;

    @Override
    public void initialize(GenreParameter parametersAnnotation , FrameworkMethod frameworkMethod) {
        this.parametersAnnotation = parametersAnnotation;
        this.parameterTypes = frameworkMethod.getMethod().getParameterTypes();
    }

    @Override
    public Object[] getParameters() {
        String[] values = parametersAnnotation.value();
        int typeLen = parameterTypes.length;

        List res = new ArrayList();
        for (String val : values) {
            String[] ps = val.split(",");
            if (ps.length != typeLen) {
                res.add(ps);
            }else{

                Object[] o = new Object[typeLen];
                for(int i=0;i<typeLen; i++) {
                    o[i] = castObject(parameterTypes[i], ps[i].trim());
                }
                res.add(o);
            }
        }
        return list2array(res);
    }
}
