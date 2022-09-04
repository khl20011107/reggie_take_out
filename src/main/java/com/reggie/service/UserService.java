package com.reggie.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.reggie.entity.User;



public interface UserService extends IService<User> {

    public void sendMsg(String to,String subject,String text);
}
