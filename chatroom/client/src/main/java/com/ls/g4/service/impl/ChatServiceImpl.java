package com.ls.g4.service.impl;

import com.ls.g4.po.Message;
import com.ls.g4.service.ChatService;
import com.ls.g4.service.UserService;

/**
 * @date 2019/8/27
 */
public class ChatServiceImpl implements ChatService {
  private UserService userService = new UserServiceImpl();
  private String chatUser;

  @Override public String chattingWithUser() {
    return chatUser;
  }

  @Override public void chatWith(String userName) {
    this.chatUser = userName;
  }

  @Override public void exitChat() {
    chatUser = null;
  }

  @Override public Message sendMessage(String msg) {
    Message message = new Message();
    message.setTime(System.currentTimeMillis());
    message.setMsg(msg);
    message.setReceiverName(chatUser);
    return message;
  }
}
