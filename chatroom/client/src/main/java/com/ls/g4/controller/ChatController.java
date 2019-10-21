package com.ls.g4.controller;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.ls.g4.client.Client;
import com.ls.g4.config.ClientProperties;
import com.ls.g4.po.Message;
import com.ls.g4.po.UserBroadcast;
import com.ls.g4.protocol.MsgProtocolV1;
import com.ls.g4.protocol.MsgType;
import com.ls.g4.service.ChatService;
import com.ls.g4.service.UserService;
import com.ls.g4.service.impl.ChatServiceImpl;
import com.ls.g4.util.JSONKey;

import java.io.UnsupportedEncodingException;

/**
 *
 * 聊天模块控制层
 */
public class ChatController {

  private Client client;

  ChatService chatService = new ChatServiceImpl();

  public UserService userService;

  public ChatController(Client client) {
    this.client = client;
  }

  public void chatWith(String userName){
      JSONObject data = new JSONObject();
      data.put(JSONKey.USER_NAME, userName);
    MsgProtocolV1.DataPacket dataPacket = null;
    try {
      dataPacket = MsgProtocolV1.protocol.new DataPacket()
          .setMsgType(MsgType.GET_HISTORY.getType()).setData(data.toJSONString().getBytes(
              ClientProperties.CHARSET));
    } catch (UnsupportedEncodingException e) {
      //编码错误不处理
    }
    client.getClientHandler().write(dataPacket);
    chatService.chatWith(userName);
  }

  public String isChattingWithUser() {
    return chatService.chattingWithUser();
  }

  public void chatOut() {
    chatService.exitChat();
    client.getView().show("你已退出会话");
  }

  public void send(String msg){
      JSONObject jsonObject = new JSONObject();
      Message message = chatService.sendMessage(msg);
      message.setSenderName(userService.getLoginUser().getName());
      jsonObject.put(JSONKey.MESSAGE, message);
    MsgProtocolV1.DataPacket dataPacket = null;
    try {
      dataPacket = MsgProtocolV1.protocol.new DataPacket()
          .setMsgType(MsgType.SEND_MESSGAGE.getType())
          .setData(jsonObject.toJSONString().getBytes(ClientProperties.CHARSET));
    } catch (UnsupportedEncodingException e) {
      //编码错误不处理
    }
    client.getClientHandler().write(dataPacket);
  }

  /**
   * 接收历史消息，并通过view层显示
   *
   * @param data
   */
  public void reviceHistory(JSONObject data) {
    String code = data.get(JSONKey.CODE).toString();
    if (JSONKey.FAIL_CODE.equals(code)) {
      client.getView().show(data.get(JSONKey.REASON).toString());
    } else {
      client.getView().show("======历史消息======");
      boolean online = data.getBoolean(JSONKey.ONLINE);
      JSONArray msgs = data.getJSONArray(JSONKey.HISTORY_LIST);
      JSONObject back = new JSONObject();
      JSONArray readMessages = new JSONArray();
      for (int i = 0; i < msgs.size(); i++) {
        JSONObject jsonObject = msgs.getJSONObject(i);
        Message message = jsonObject.toJavaObject(Message.class);
        if (message.getStatus() == 0){
          readMessages.add(message.getId());
        }
        if (message.getSenderName().equals(userService.getLoginUser().getName())){
          client.getView().showMessage(message,true);
        }else{
          client.getView().showMessage(message,false);
        }
      }
      client.getView().show("对方处于" + ((online) ? "在线" : "离线") + "状态");
      if (readMessages.size()>0){
        back.put(JSONKey.MESSAGE_READ,readMessages);
        MsgProtocolV1.DataPacket dataPacket = null;
        try {
          dataPacket = MsgProtocolV1.protocol.new DataPacket()
              .setMsgType(MsgType.MESSAGE_BACK.getType())
              .setData(back.toJSONString().getBytes(ClientProperties.CHARSET));
        } catch (UnsupportedEncodingException e) {
          //编码错误不处理
        }
        client.getClientHandler().write(dataPacket);
      }
    }
  }

  /**
   * 接受转发的消息
   *
   * @param data 转发消息数据
   */
  public void reviceMessage(JSONObject data) {
    Message message = data.getJSONObject(JSONKey.MESSAGE).toJavaObject(Message.class);
    if (message.getSenderName().equals(chatService.chattingWithUser())) {
      message.setStatus(1);
      client.getView().showMessage(message,false);
      JSONObject back = new JSONObject();
      JSONArray readMessages = new JSONArray();
      readMessages.add(message.getId());
      back.put(JSONKey.MESSAGE_READ,readMessages);
      MsgProtocolV1.DataPacket dataPacket = null;
      try {
        dataPacket = MsgProtocolV1.protocol.new DataPacket()
            .setMsgType(MsgType.MESSAGE_BACK.getType())
            .setData(back.toJSONString().getBytes(ClientProperties.CHARSET));
      } catch (UnsupportedEncodingException e) {
        //编码错误不处理
      }
      client.getClientHandler().write(dataPacket);
    }
  }

  /**
   * 接收广播
   *
   * @param data
   */
  public void reviceBroadcat(JSONObject data) {
    UserBroadcast broadcast = data.getJSONObject(JSONKey.BROADCAST)
        .toJavaObject(UserBroadcast.class);
    if (broadcast.getUserName().equals(chatService.chattingWithUser())) {
      client.getView().show(broadcast.getUserName() + ((broadcast.isState()) ? " 上线了" : " 下线了"));
    }
  }
}
