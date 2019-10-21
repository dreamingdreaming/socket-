package com.ls.g4.client;

import com.ls.g4.controller.ChatController;
import com.ls.g4.controller.UserController;
import com.ls.g4.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * 客户端启动运行停止
 */
public class Client {

  private final String host;

  private final int port;

  private Socket socket;

  private InputStream in;

  private OutputStream out;

  UserController userController;

  ChatController chatController;

  private  ClientHandler clientHandler;

  private View view;

  private volatile boolean reconnecting;

  public Client(String host, int port) {
    this.host = host;
    this.port = port;
    this.view = new View();
  }

  public View getView() {
    return view;
  }

  public ClientHandler getClientHandler(){
    return clientHandler;
  }

  public OutputStream getOut() {
    return out;
  }

  public InputStream getIn() {
    return in;
  }

  public void start() {
    try {
      socket = new Socket(host, port);
      in = socket.getInputStream();
      out = socket.getOutputStream();
      view.show("连接服务器成功");
      clientHandler = new ClientHandler(this);
      userController = new UserController(this);
      chatController = new ChatController(this);
      chatController.userService = userController.userService;
      run();
    } catch (UnknownHostException e) {
      view.showError("未知的主机地址");
    } catch (IOException e) {
      view.showError("网络异常");
    }
  }

  private void run() {
    try {
      userController.login();
      //开启读线程
      clientHandler.start();
      //循环接收指令
      receiveCmd();
      stop();
    }catch (IOException e){
      reconnect();
    }
  }

  private void receiveCmd(){
    String input;
    while (true) {
      input = view.input();
      if ("exit".equals(input)) {
        userController.logOut();
        break;
      } else {
        String[] split = input.split("\\s+");
        if ("chat".equals(split[0])) {
          String chatUserName = split[1];
          view.show("starting chat..");
          chatController.chatWith(chatUserName);
          while (true) {
            input = view.input();
            if (!":q".equals(input)) {
              chatController.send(input);
            } else {
              chatController.chatOut();
              break;
            }
          }
        } else {
          view.showError("输入错误..");
        }
      }
    }
  }

  void reconnect() {
    try {
      if(!reconnecting) {
        try {
          Thread.sleep(6000);
        } catch (InterruptedException e) {
          e.printStackTrace();
        }
        reconnecting = true;
        Socket reSocket = new Socket(host, port);
        this.socket = reSocket;
        in = socket.getInputStream();
        out = socket.getOutputStream();
        if (userController.reLogin()) {
          view.show("重新连接成功");
        } else {
          view.show("重新连接失败");
        }
        reconnecting = false;
      }
    } catch (IOException e) {
      view.showError("重新连接失败。。网络异常");
    }
  }

  private void stop() {
    try {
      clientHandler.interrupt();
      if (in != null) {
        in.close();
      }
      if (out != null) {
        out.close();
      }
      if (socket != null) {
        socket.close();
      }
    } catch (IOException e) {
      //关闭就不处理了
    }

  }
}
