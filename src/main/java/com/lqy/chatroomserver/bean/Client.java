package com.lqy.chatroomserver.bean;

import java.net.Socket;

/**
 * 一句话功能描述
 * 功能详细描述
 *
 * @author Lv Qingyang
 * @date 2018/6/25
 * @email biloba12345@gamil.com
 * @github https://github.com/biloba123
 * @blog https://biloba123.github.io/
 * @see
 * @since
 */
public class Client {
    public int userId;
    public String username;
    public Socket socket;

    public Client(int userId, String username, Socket socket) {
        this.userId = userId;
        this.username = username;
        this.socket = socket;
    }
}
