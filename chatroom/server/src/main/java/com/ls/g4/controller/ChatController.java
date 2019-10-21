package com.ls.g4.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ls.g4.config.ServerProperties;
import com.ls.g4.po.Message;
import com.ls.g4.po.User;
import com.ls.g4.protocol.MsgProtocolV1;
import com.ls.g4.protocol.MsgType;
import com.ls.g4.server.ServerHandler;
import com.ls.g4.service.ChatService;
import com.ls.g4.service.UserService;
import com.ls.g4.service.impl.ChatServiceImpl;
import com.ls.g4.util.JSONKey;

import java.io.IOException;
import java.util.List;

public class ChatController {
  ChatService chatService = new ChatServiceImpl();

  UserService userService;

  ChatController(UserService userService) {
    this.userService = userService;
  }

  /**
   * 转发消息
   * @param data
   * @param handler
   */
  public void forwardMessage(JSONObject data, ServerHandler handler) {
    Message message = data.getJSONObject(JSONKey.MESSAGE).toJavaObject(Message.class);
    int mid = chatService.saveMessage(message);
    JSONObject forwardMsg = new JSONObject();
    message.setId(mid);
    forwardMsg.put(JSONKey.MESSAGE, message);
    handler.write(forwardMsg, false);
  }

  /**
   * 消息已读返回
   * @param data
   */
  public void messageBack(JSONObject data){
    JSONArray readMessages = data.getJSONArray(JSONKey.MESSAGE_READ);
    List<Integer> messagesId = readMessages.toJavaList(Integer.TYPE);
    chatService.saveMessageReaded(messagesId);
  }

  /**
   * @return 返回两人最近的20条历史消息
   */
  public void chatwith(JSONObject data, ServerHandler serverHandler) throws IOException {
    try {
      User u1 = new User();
      User u2 = new User();
      u1.setName(serverHandler.getUser().getName());
      u2.setName(data.get(JSONKey.USER_NAME).toString());
      JSONObject backObjet = new JSONObject();
      if (true || userService.exist(u2)) {
        List<Message> msgList = chatService.chatWith(u1, u2);
        boolean online = userService.isOnline(u2);
        backObjet.put(JSONKey.ONLINE, online);
        backObjet.put(JSONKey.CODE, JSONKey.SUCCESS_CODE);
        backObjet.put(JSONKey.LIST_SIZE, msgList.size());
        JSONArray msgs = new JSONArray();
        msgList.forEach(msg -> {
          msgs.add(msg);
        });
        backObjet.put(JSONKey.HISTORY_LIST, msgs);
      } else {
        backObjet.put(JSONKey.CODE, JSONKey.SUCCESS_CODE);
        backObjet.put(JSONKey.REASON, "用户不存在");
      }
      MsgProtocolV1.DataPacket dataPacket = MsgProtocolV1.protocol.new DataPacket()
          .setMsgType(MsgType.GET_HISTORY.getType()).setData(backObjet.toJSONString().getBytes(
              ServerProperties.CHARSET));
      serverHandler.getOut().write(dataPacket.toBytes());
    } catch (IOException e) {
      e.printStackTrace();
      throw  e;
    }
  }

}
