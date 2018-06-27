package com.lqy.chatroomserver;

import com.lqy.chatroomserver.socket.ChatroomSocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ChatroomServerApplication {
    private static final int SOCKET_PORT = 6666;
    
    public static void main(String[] args) {
        SpringApplication.run(ChatroomServerApplication.class, args);
        ChatroomSocket chatroomSocket=new ChatroomSocket(SOCKET_PORT);
        chatroomSocket.start();
    }
}
