package com.ls.g4.client;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.ls.g4.config.ClientProperties;
import com.ls.g4.protocol.MsgProtocolV1;
import com.ls.g4.protocol.MsgType;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

/**
 * @date 2019/8/29
 */
public class ClientHandler extends Thread {
  private Client client;

  public ClientHandler(Client client) {
    this.client = client;
  }

  @Override public void run() {
    while (!isInterrupted()) {
      try {
        MsgProtocolV1.DataPacket dataPacket = MsgProtocolV1.protocol
            .readDataPacketFromStream(client.getIn());
        read(dataPacket);
      } catch (IOException e) {
        if (isInterrupted()) {
          break;
        }
       client.reconnect();
      }
    }
  }

  private void read(MsgProtocolV1.DataPacket dataPacket) {
    int msgType = dataPacket.getMsgType();
    JSONObject jsonObject = null;
    try {
      jsonObject = JSON.parseObject(new String(dataPacket.getData(), ClientProperties.CHARSET));
    } catch (UnsupportedEncodingException e) {
      //编码错误不处理
    }
    if (msgType == MsgType.GET_HISTORY.getType()) {
      client.chatController.reviceHistory(jsonObject);
    } else if (msgType == MsgType.SEND_MESSGAGE.getType()) {
      client.chatController.reviceMessage(jsonObject);
    } else if (msgType == MsgType.SYSTEM_BROADCAST.getType()) {
      client.chatController.reviceBroadcat(jsonObject);
    }
  }

  public void write(MsgProtocolV1.DataPacket dataPacket) {
    try {
      client.getOut().write(dataPacket.toBytes());
    } catch (IOException e) {
      System.out.println("having error!");
     client.reconnect();
    }
  }

}
