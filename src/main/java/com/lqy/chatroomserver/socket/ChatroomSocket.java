package com.lqy.chatroomserver.socket;

import com.google.gson.Gson;
import com.lqy.chatroomserver.bean.MyMessage;
import com.lqy.chatroomserver.bean.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * 一句话功能描述
 * 功能详细描述
 *
 * @author Lv Qingyang
 * @date 2018/6/23
 * @email biloba12345@gamil.com
 * @github https://github.com/biloba123
 * @blog https://biloba123.github.io/
 * @see
 * @since
 */
public class ChatroomSocket {
    private int mPort;
    private ServerSocket mServerSocket;
    /**
     * 已连接的client
     */
    private List<Socket> mSocketList;
    private List<User> mUserList;
    /**
     * 将Client和PrintWriter保存为键值对，免得每次发消息都要重新获得PrintWriter
     */
    private Map<Socket, PrintWriter> mClientPrintWriterMap;
    private ExecutorService mExecutor;
    private Gson mGson = new Gson();
    /**
     * 客户端退出标志
     */
    private static final String FLAG_EXIT = "$exit$";
    /**
     * 消息结束位标志，防止多行消息被拆分
     */
    private static final String FLAG_END = "$end$";

    public ChatroomSocket(int port) {
        this.mPort = port;
        mSocketList = new ArrayList<>();
        mUserList=new ArrayList<>();
        mClientPrintWriterMap = new HashMap<>();
        mExecutor = Executors.newCachedThreadPool();
    }

    public void start() {
        try {
            mServerSocket = new ServerSocket(mPort);
            Socket socket = null;
            ServiceRunnable serviceRunnable = null;
            System.out.println("Server started, wait for client...");

            while (true) {
                socket = mServerSocket.accept();
                try {
                    mExecutor.execute(new ServiceRunnable(socket));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    class ServiceRunnable implements Runnable {
        private Socket mSocket;
        private User mUser;
        private BufferedReader mReader;
        private PrintWriter mWriter;
        private String mMessage;
        private StringBuilder mSb;

        public ServiceRunnable(Socket socket) throws Exception {
            mSocket=socket;
            mReader = new BufferedReader(new InputStreamReader(mSocket.getInputStream(), "UTF-8"));
            //先等client将自己的用户信息发过来
            if ((mMessage = mReader.readLine()) != null) {
                mUser = mGson.fromJson(mMessage, User.class);

                mSocketList.add(mSocket);
                mUserList.add(mUser);
                mWriter=new PrintWriter(new OutputStreamWriter(mSocket.getOutputStream(), "UTF-8"), true);
                mClientPrintWriterMap.put(socket, mWriter);

                //将在线用户发给client
                mWriter.println(mGson.toJson(mUserList));

                //发送该用户进入聊天室的消息
                sendMessage(new MyMessage(MyMessage.TYPE_ARRVIDE, mUser, null).toString());
            }
        }

        @Override
        public void run() {
            try {
                while (true) {
                    if (!mSocket.isClosed()) {
                        if (mSocket.isConnected()) {
                            if ((mMessage = mReader.readLine()) != null) {
                                if (FLAG_EXIT.equals(mMessage)) {
                                    System.out.println("exit");
                                    break;
                                }else {
                                    if (mSb == null) {
                                        mSb = new StringBuilder();
                                    }
                                    if (mMessage.endsWith(FLAG_END)) {
                                        mSb.append(mMessage, 0, mMessage.length() - 5);
                                        mMessage = mSb.toString();
                                        mSb = null;

                                        sendMessage(mMessage);
                                    } else {
                                        mSb.append(mMessage + "\n");
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                try {
                    stopService();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        private void stopService() throws IOException {
            mReader.close();
            mWriter.close();
            mClientPrintWriterMap.remove(mSocket);
            mMessage = new MyMessage(MyMessage.TYPE_EXIT, mUser, null).toString();
            mSocket.close();
            mUserList.remove(mUser);
            mSocketList.remove(mSocket);
            sendMessage(mMessage);
        }
    }

    public void sendMessage(String msg) {
        System.out.println(msg);
        if (mServerSocket != null) {
            for (Socket socket : mSocketList) {
                mClientPrintWriterMap.get(socket).println(msg + FLAG_END);
            }
        }
    }

    public void destory() {
        try {
            for (Socket socket : mSocketList) {
                socket.close();
            }
            mExecutor.shutdownNow();
            mSocketList.clear();
            mServerSocket.close();
            mServerSocket = null;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}


