package com.gethin.extension.example1;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/4/5 15:09
 * @description:
 */
public class StudentImpl implements Greeting {
    private String name;

    @Override
    public void sayHello() {
        System.out.println("Hello World,"+this.name);
    }

    public void init(){
        this.name="student";
    }

    @Override
    public String toString() {
        return "HelloWorldImpl{" +
                "name='" + name + '\'' +
                '}';
    }
}
