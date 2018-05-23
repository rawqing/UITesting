package com.jlc.app.milk_mini.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Stack;

/**
 * Created by king on 2016/9/9.
 */
public class Registor {

    private static Map<String,Stack> register = new HashMap<String,Stack>();


    /**
     * 将给定的对象按key进行注册
     * 值得注意的是,这里后进先出的方式,即压栈
     * @param key
     * @param obj
     * @return 注册成败
     */
    public static boolean reg(String key , Object obj){
        try {
            if(register.containsKey(key)){
                register.get(key).push(obj);
            }else{
                Stack stack = new Stack();
                stack.push(obj);
                register.put(key,stack);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 注册所有
     * @param key
     * @param stack
     * @return
     */
    public static boolean regAll(String key, Stack stack){
        try {
            register.put(key,stack);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 将指定key的最上层的对象注销
     * @param key
     * @return
     */
    public static Object unReg(String key){
        Stack stack ;
        Object obj = null;

        try {
            stack = register.get(key);
            obj = stack.pop();
            if (stack.empty()){     //若栈中再无注册信息,则清理寄存器
                register.remove(key);
            }
        } catch (Exception e) {
            return null;
        }
        return obj;
    }

    /**
     * 注销指定key的所以绑定
     * @param key
     * @return
     */
    public static Stack unRegAll(String key){
        try {
            return register.remove(key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }

    /**
     * 窥探指定key栈顶的对象
     * @param key
     * @return
     */
    public static Object peekReg(String key){
        Stack stack ;
        Object obj ;
        try {
            stack = register.get(key);
            obj = stack.peek();         //窥探一下 , 不注销
        } catch (Exception e) {
            return null;
        }
        return obj;
    }
}
