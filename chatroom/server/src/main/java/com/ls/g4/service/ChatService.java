package com.ls.g4.service;

import com.ls.g4.po.Message;
import com.ls.g4.po.User;

import java.util.List;

/**
 * 聊天模块的业务层接口
 */
public interface ChatService {
  /**
   * 用户1 进入与 用户2 的会话时
   *返回20条历史消息
   * @param u1
   * @param u2
   * @return 历史消息，返回最大20条
   */
  List<Message> chatWith(User u1, User u2);

  /**
   * 保存用户消息
   *
   * @param message
   * @return message id
   */
  int saveMessage(Message message);

  /**
   * 保存消息已读
   * @param mids
   */
  void saveMessageReaded(List<Integer> mids);
}
