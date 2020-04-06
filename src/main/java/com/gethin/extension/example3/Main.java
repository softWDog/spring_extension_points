package com.gethin.extension.example3;

import com.gethin.extension.example3.mapper.PersonMapper;
import com.gethin.extension.example3.mapper.UserMapper;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/4/6 1:34
 * @description:
 */
public class Main {
    public static void main(String args[]) {
        ApplicationContext applicationContext=new ClassPathXmlApplicationContext("example3.xml");
        UserMapper userMapper= applicationContext.getBean(UserMapper.class);
        userMapper.getUser(1);
        PersonMapper personMapper=applicationContext.getBean(PersonMapper.class);
        personMapper.getPerson();
    }
}
