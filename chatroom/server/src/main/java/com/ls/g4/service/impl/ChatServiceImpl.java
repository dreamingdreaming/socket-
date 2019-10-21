package com.ls.g4.service.impl;

import com.ls.g4.mapper.MsgDao;
import com.ls.g4.po.Message;
import com.ls.g4.po.User;
import com.ls.g4.service.ChatService;
import com.ls.g4.util.SqlSessionUtil;

import java.util.Collections;
import java.util.List;

public class ChatServiceImpl implements ChatService {
  private final int HISTORY_MAX_SIZE = 20;
  MsgDao msgDao = SqlSessionUtil.getSqlSession().getMapper(MsgDao.class);

  /**
   * 返回历史记录
   *
   * @param u1
   * @param u2
   * @return
   */
  @Override public List<Message> chatWith(User u1, User u2) {
    List<Message> messages = msgDao.queryMessages(u1, u2, HISTORY_MAX_SIZE);
    Collections.reverse(messages);
    return messages;
  }

  /**
   * 添加message信息
   *
   * @param message
   */
  @Override public int saveMessage(Message message) {
    msgDao.addMsg(message);
    return message.getId();
  }

  @Override public void saveMessageReaded(List<Integer> mids) {
    mids.forEach(mid->{
      msgDao.updateMsg(mid);
    });
  }
}
