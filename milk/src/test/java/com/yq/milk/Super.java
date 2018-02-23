package com.yq.milk;

/**
 * Created by king on 2018/2/23.
 */

public abstract class Super {

    int age = 19;
    String name;

    public Super(){
        this.name = way();
    }

    public void say(String s){
        System.out.println( s + " "+age+" "+ name);
    }

    public abstract String way();
}
