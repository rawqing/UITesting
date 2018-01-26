package com.yq.milk.custom.parameters.provider;


import com.yq.milk.custom.parameters.PickParameter;

import org.junit.runners.model.FrameworkMethod;

import junitparams.custom.ParametersProvider;

/**
 * Created by king on 2017/7/7.
 */

public class PickParameterProvider implements ParametersProvider<PickParameter> {
    private PickParameter parametersAnnotation;

    @Override
    public void initialize(PickParameter parametersAnnotation , FrameworkMethod frameworkMethod) {

    }

    @Override
    public Object[] getParameters() {
        return new Object[0];
    }
}
