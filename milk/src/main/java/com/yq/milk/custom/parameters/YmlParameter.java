package com.yq.milk.custom.parameters;

import com.trubuzz.trubuzz.feature.custom.parameters.provider.YmlParameterProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import junitparams.custom.CustomParameters;

/**
 * Created by king on 2017/7/14.
 */
@Retention(RetentionPolicy.RUNTIME)
@CustomParameters(provider = YmlParameterProvider.class)
public @interface YmlParameter {

    /**
     * 指定 yml 文件的文件名 , 需加上扩展名
     *      不要写入路径
     * @return
     */
    String value() default "";

}
