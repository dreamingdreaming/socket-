package com.ls.g4.server;

import com.alibaba.fastjson.JSONObject;
import com.ls.g4.cache.UserCache;
import com.ls.g4.controller.DispathService;
import com.ls.g4.po.Message;
import com.ls.g4.po.User;
import com.ls.g4.protocol.MsgProtocolV1;
import com.ls.g4.protocol.MsgProtocolV1.DataPacket;
import com.ls.g4.protocol.MsgType;
import com.ls.g4.util.JSONKey;
import com.ls.g4.config.ServerProperties;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.util.concurrent.TimeUnit;

/**
 * 与客户端进行通信
 */
public class ServerHandler extends Thread {

  private Socket client;

  private InputStream in;

  private OutputStream out;

  private DispathService dispathServer;

  private User user;

  private UserCache userCache;

  private volatile boolean reconnected;

  public ServerHandler(Socket client, DispathService dispathServer) throws IOException {
    this.client = client;
    this.dispathServer = dispathServer;
    this.userCache = dispathServer.userController.getUserCache();
    in = client.getInputStream();
    out = client.getOutputStream();
  }

  public OutputStream getOut() {
    return out;
  }

  public Socket getClient() {
    return client;
  }

  public User getUser() {
    return user;
  }

  public void setUser(User user) {
    this.user = user;
    System.out.println(user.getName() + "登陆成功");
  }

  @Override public void run() {
    while (!Thread.currentThread().isInterrupted()) {
      try {
        DataPacket dataPacket = MsgProtocolV1.protocol.readDataPacketFromStream(in);
        dispathServer.dispath(dataPacket, this);
      } catch (IOException e) {
        if (user !=null && !isInterrupted()){
          waitReconnect();
        }
        break;
      }
    }
    System.out.println(client.getInetAddress().getHostAddress()+":"+client.getPort()+" 断开了连接");
  }

  private boolean waitReconnect() {
    if (reconnected) {
      return true;
    }
    System.out.println(user.getName() + " 掉线了...等待重新连接");
    long t1 = System.currentTimeMillis();
    synchronized (this) {
      try {
        wait(TimeUnit.SECONDS.toMillis(ServerProperties.timeOut));
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    long t2 = System.currentTimeMillis();
    if (TimeUnit.MILLISECONDS.toSeconds(t2 - t1) >= ServerProperties.timeOut && !reconnected) {
      dispathServer.userController.logout(this);
      System.out.println(user.getName() + "退出了");
      return false;
    } else {
      return true;
    }
  }

  public void reconnect() {
    reconnected = true;
    synchronized (this) {
      notifyAll();
    }
    System.out.println(user.getName() + "重新连接成功");
  }

  public void write(JSONObject data, boolean broadcast) {
    if (broadcast) {
      userCache.getAllUser().values().stream().filter(
          handler -> !handler.getClient().isOutputShutdown() && !handler.getClient().isClosed())
          .forEach(handler -> {
            try {
              DataPacket dataPacket = MsgProtocolV1.protocol.new DataPacket()
                  .setMsgType(MsgType.SYSTEM_BROADCAST.getType())
                  .setData(data.toJSONString().getBytes(ServerProperties.CHARSET));
              handler.out.write(dataPacket.toBytes());
            } catch (IOException e) {
              //发送失败 就算了
            }
          });
    } else {
      Message message = data.getJSONObject(JSONKey.MESSAGE).toJavaObject(Message.class);
      ServerHandler handler = userCache.getAllUser().get(message.getReceiverName());
      if (handler != null && !handler.getClient().isOutputShutdown() && !handler.getClient()
          .isClosed()) {
        try {
          DataPacket dataPacket = MsgProtocolV1.protocol.new DataPacket()
              .setMsgType(MsgType.SEND_MESSGAGE.getType()).setData(data.toJSONString()
                  .getBytes(ServerProperties.CHARSET));
          handler.out.write(dataPacket.toBytes());
        } catch (IOException e) {
          //发送失败 就算了
        }
      }
    }
  }
}
