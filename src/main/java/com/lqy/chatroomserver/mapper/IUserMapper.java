package com.lqy.chatroomserver.mapper;

import com.lqy.chatroomserver.bean.User;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

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
@Mapper
public interface IUserMapper {
    @Insert("insert into user values(null, #{username}, #{password})")
    int addUser(User user);

    @Select("select * from user where username=#{username} and password=#{password}")
    User findUser(User user);

    @Select("select * from user where username=#{username}")
    User findUserByUsername(String username);
}
