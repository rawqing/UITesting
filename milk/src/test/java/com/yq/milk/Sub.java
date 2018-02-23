package com.yq.milk;

/**
 * Created by king on 2018/2/23.
 */

public class Sub extends Super{
    String fName;

    public Sub(String fName, String lName ) {
        this.fName = fName;
        this.lName = lName;
        this.name = way();
    }

    public Sub(){}
    String lName;



    public String way(){
        return this.fName + this.lName;
    }
}
