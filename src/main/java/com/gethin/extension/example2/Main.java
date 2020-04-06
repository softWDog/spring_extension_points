package com.gethin.extension.example2;

import com.gethin.extension.example1.Greeting;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/3/15 22:13
 * @description:
 */
public class Main {
    public static void main(String args[]) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("example2.xml");
        User user= (User) applicationContext.getBean("user");
        System.out.println(user.toString());
    }
}
