package com.hj.reggie.front.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.hj.reggie.front.bean.User;
import com.hj.reggie.front.mapper.UserMapper;
import com.hj.reggie.front.service.UserService;
import org.springframework.stereotype.Service;

/**
 * @create 2023-03-12 14:53
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
}
