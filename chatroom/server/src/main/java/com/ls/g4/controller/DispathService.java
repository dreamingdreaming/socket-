package com.ls.g4.controller;

import com.alibaba.fastjson.JSONObject;
import com.ls.g4.config.ServerProperties;
import com.ls.g4.protocol.MsgProtocolV1.DataPacket;
import com.ls.g4.protocol.MsgType;
import com.ls.g4.server.ServerHandler;

import java.io.IOException;

/**
 * 分发请求给各个service,并将
 */
public class DispathService {

  public UserController userController;
  public ChatController chatController;

  public DispathService() {
    userController = new UserController();
    chatController = new ChatController(userController.userService);
  }

  public void dispath(DataPacket dataPacket, ServerHandler serverHandler) throws IOException {
    try {
      JSONObject data = JSONObject.parseObject(new String(dataPacket.getData(), ServerProperties.CHARSET));
      int msgType = dataPacket.getMsgType();
      if (msgType == MsgType.LOGIN.getType()) {
        userController.login(data, serverHandler);
      } else if(msgType == MsgType.LOGOUT.getType()){
        userController.logout(serverHandler);
      } else if (msgType == MsgType.GET_HISTORY.getType()) {
        chatController.chatwith(data, serverHandler);
      } else if (msgType == MsgType.SEND_MESSGAGE.getType()) {
        chatController.forwardMessage(data, serverHandler);
      } else if (msgType == MsgType.MESSAGE_BACK.getType()){
        chatController.messageBack(data);
      }
    } catch (IOException e) {
      throw  e;
    }
  }
}
