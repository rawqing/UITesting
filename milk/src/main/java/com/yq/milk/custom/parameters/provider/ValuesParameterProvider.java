package com.yq.milk.custom.parameters.provider;

import com.trubuzz.trubuzz.feature.custom.parameters.ValuesParameter;

import org.junit.runners.model.FrameworkMethod;

import junitparams.custom.ParametersProvider;

/**
 * Created by king on 2017/7/7.
 */

public class ValuesParameterProvider implements ParametersProvider<ValuesParameter> {
    private ValuesParameter parametersAnnotation;

    @Override
    public void initialize(ValuesParameter parametersAnnotation , FrameworkMethod frameworkMethod) {
        this.parametersAnnotation = parametersAnnotation;
    }

    @Override
    public Object[] getParameters() {
        String[] values = parametersAnnotation.value();
        return values;
    }
}
