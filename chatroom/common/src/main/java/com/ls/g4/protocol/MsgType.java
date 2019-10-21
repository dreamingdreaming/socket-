package com.ls.g4.protocol;

/**
 * 不同请求的标识号
 */
public enum MsgType {
  /**
   * 系统广播
   */
  SYSTEM_BROADCAST(0),
  /**
   * 用户发送消息
   */
  SEND_MESSGAGE(1),
  /**
   * 获取历史信息
   */
  GET_HISTORY(2),
  /**
   * 登录请求
   */
  LOGIN(3),
  /**
   * 退出
   */
  LOGOUT(4),
  /**
   * 登录返回结果
   */
  LOGIN_BACK(5),
  /**
   * 已读消息返回
   */
  MESSAGE_BACK(6);

  private final int type;

  private MsgType(int type) {
    this.type = type;
  }

  public int getType() {
    return type;
  }
}
