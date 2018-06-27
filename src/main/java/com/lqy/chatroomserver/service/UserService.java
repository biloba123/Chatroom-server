package com.lqy.chatroomserver.service;

import com.lqy.chatroomserver.bean.User;
import com.lqy.chatroomserver.mapper.IUserMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 一句话功能描述
 * 功能详细描述
 *
 * @author Lv Qingyang
 * @date 2018/6/24
 * @email biloba12345@gamil.com
 * @github https://github.com/biloba123
 * @blog https://biloba123.github.io/
 * @see
 * @since
 */
@Service
public class UserService {
    private IUserMapper mUserMapper;

    @Autowired
    public UserService(IUserMapper userMapper) {
        mUserMapper = userMapper;
    }

    public boolean addUser(User user){
        return mUserMapper.addUser(user)==1;
    }

    public User findUser(User user){
        return mUserMapper.findUser(user);
    }

    public User findUserByUsername(String username){
        return mUserMapper.findUserByUsername(username);
    }
}
