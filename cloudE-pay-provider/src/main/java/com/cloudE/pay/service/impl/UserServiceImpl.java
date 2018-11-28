package com.cloudE.pay.service.impl;

import com.cloudE.entity.User;
import com.cloudE.mapper.UserMapper;
import com.cloudE.mapper1.UserMapper1;
import com.cloudE.pay.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    UserMapper userMapper;

    @Autowired
    UserMapper1 userMapper1;


    @Override
    @Transactional(transactionManager = "atomikosTx",propagation = Propagation.REQUIRED,rollbackFor = {RuntimeException.class})
    public boolean addUser(User user) {
        int a = userMapper.insertSelective(user);
        int b = userMapper1.insertSelective(user);
        int i = 1/0;
        return a > 0 && b > 0;
    }
}
