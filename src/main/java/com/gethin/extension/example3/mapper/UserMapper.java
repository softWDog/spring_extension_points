package com.gethin.extension.example3.mapper;

import com.gethin.extension.example3.annotation.Mapper;

/**
 * Created with IntelliJ IDEA.
 *
 * @author: gethin
 * @email: denggx3@chinaunicom.cn
 * @Date: 2020/4/6 0:51
 * @description:
 */
@Mapper
public interface UserMapper {
    void getUser(int id);
}
