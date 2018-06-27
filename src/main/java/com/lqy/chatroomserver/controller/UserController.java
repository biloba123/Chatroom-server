package com.lqy.chatroomserver.controller;

import com.lqy.chatroomserver.bean.User;
import com.lqy.chatroomserver.service.UserService;
import com.lqy.chatroomserver.util.TextUtils;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
@RestController
@RequestMapping("/user")
public class UserController {
    @Autowired
    private UserService mUserService;

    @RequestMapping("/register")
    public int register(@RequestBody User user){
        if (user==null || TextUtils.isEmpty(user.getUsername()) || TextUtils.isEmpty(user.getPassword())) {
            return -1;
        }

        if (mUserService.findUserByUsername(user.getUsername()) != null) {
            return 0;
        }


        if (mUserService.addUser(user)) {
            return mUserService.findUser(user).getId();
        }
        return -1;
    }

    @RequestMapping("/login")
    public User login(@RequestBody User user){
        return mUserService.findUser(user);
    }
}
