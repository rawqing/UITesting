package com.yq.milk.custom.parameters;


import com.yq.milk.custom.parameters.provider.GenreParameterProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import junitparams.custom.CustomParameters;

/**
 * Created by king on 2017/7/10.
 */
@Retention(RetentionPolicy.RUNTIME)
@CustomParameters(provider = GenreParameterProvider.class)
public @interface GenreParameter {

    String[] value();
}
