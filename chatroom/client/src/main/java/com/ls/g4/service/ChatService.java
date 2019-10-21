package com.ls.g4.service;

import com.ls.g4.po.Message;

/**
 * 2019年8月26日
 * 聊天模块业务层接口
 */
public interface ChatService {
  /**
   * 进入聊天会话
   *
   * @param userName
   */
  void chatWith(String userName);

  /**
   * 获取正在聊天的用户
   *
   * @return 正在聊天的用户
   */
  String chattingWithUser();

  /**
   * 退出会话
   */
  void exitChat();

  /**
   * 发送消息
   *
   * @param msg
   * @return 返回包装的message
   */
  Message sendMessage(String msg);
}
