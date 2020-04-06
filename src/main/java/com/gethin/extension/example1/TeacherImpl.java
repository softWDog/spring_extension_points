package com.gethin.extension.example1;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/4/5 17:06
 * @description:
 */
public class TeacherImpl implements Greeting {
    private String name;

    @Override
    public void sayHello() {
        System.out.println("Hello World,"+this.name);
    }

    public void init(){
        this.name="teacher";
    }

    @Override
    public String toString() {
        return "TeacherImpl{" +
                "name='" + name + '\'' +
                '}';
    }
}
