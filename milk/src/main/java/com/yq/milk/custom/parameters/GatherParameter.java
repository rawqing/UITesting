package com.yq.milk.custom.parameters;

import com.trubuzz.trubuzz.feature.custom.parameters.provider.GatherParameterProvider;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import junitparams.custom.CustomParameters;

/**
 * Created by king on 2017/7/3.
 */
@Retention(RetentionPolicy.RUNTIME)
@CustomParameters(provider = GatherParameterProvider.class)
public @interface GatherParameter {

    /**
     * 参数为 map 或 list 的字符串形式
     * 可简化书写 , 可使用混合模式 ( 但不支持对象类型 , 只支持基础数据类型 )
     * 如 Map写法 :
     *      "{n:18.12 ,m:'Ann'}" 这里 n 将是java.math.BigDecimal , m将是java.lang.String
     *                          若 n 为整数则为 java.lang.Integer 类型
     *      "{\"n\":18,\"m\":\"Ann\"}"       这种为标准的 Json 写法
     * List 写法相对简单:
     *      "[18,'Ann']"         取值类型解释同 Map
     * List<Map> 写法:
     *      {"{n:18 ,m:'Ann'}","{n:20,m:'Jack'}"}
     *                          需使用数组字符串 , 其内部的每个子串格式必须正确 .
     * List<List> 写法:
     *      {"[12,23]","[11,22]"}
     *                          需使用数组字符串 , 其内部的每个子串格式必须正确 .
     *
     * 注:   在使用泛型List的时候 ,必须注意其子串格式必须一致 , 否则在注入参数的时候
     *      将会出现类型强转异常.
     *      初始值只有 String 类型可以为空串 '' , Integer类型则不能为空 必须设定初始值为 0 或其他.
     * @return 当前只支持 Map , List 和 List<Map> \ List<List>,
     *          其他类型写法将会 return value 原值
     *          当然 , Map 和 List 的字符串形式格式出现错误将会抛出异常
     */
    String[] value() default {};
}
