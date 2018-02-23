package com.yq.milk.elements;

import com.yq.milk.shell.AdViewInteraction;

/**
 * Created by king on 16/11/8.
 */

public interface Element<E>{

    /**
     * 查找元素的方式
     * @return
     */
    E way();

    /**
     * 返回可操作的实例封装对象
     * @return
     */
    AdViewInteraction goal();

}
