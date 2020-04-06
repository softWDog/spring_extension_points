package com.gethin.extension.example1;

import com.gethin.extension.example2.User;
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
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("example1.xml");
        Greeting student= (Greeting) applicationContext.getBean("student");
        student.sayHello();
        Greeting teacher= (Greeting) applicationContext.getBean("teacher");
        teacher.sayHello();
        User user= (User) applicationContext.getBean("user1");
        System.out.println(user.toString());
    }

}
