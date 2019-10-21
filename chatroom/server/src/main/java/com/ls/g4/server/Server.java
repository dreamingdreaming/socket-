package com.ls.g4.server;

import com.ls.g4.controller.DispathService;
import com.ls.g4.config.ServerProperties;

import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

public class Server {
  private ServerSocket serverSocket;

  private ExecutorService executorService;

  public void start() {
    try {
      serverSocket = new ServerSocket(ServerProperties.port);
      System.out.println("服务器启动成功！监听端口：" + ServerProperties.port);
      executorService = new ThreadPoolExecutor(10, ServerProperties.maxOnline, 60, TimeUnit.SECONDS,
          new ArrayBlockingQueue<>(10));
      run();
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void run() {
    DispathService dispathService = new DispathService();
    while (true) {
      try {
        Socket socket = serverSocket.accept();
        InetAddress ip = socket.getInetAddress();
        System.out.println("接收到来自 " + ip.getHostAddress() + ":" + socket.getPort() + " 的客户端连接请求");
        ServerHandler handler = new ServerHandler(socket, dispathService);
        executorService.execute(handler);
        //客户端连接时出现异常，忽略
      } catch (Exception e) {
      }
    }
  }
}
