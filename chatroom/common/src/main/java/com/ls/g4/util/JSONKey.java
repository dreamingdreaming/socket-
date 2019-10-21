package com.ls.g4.util;

/**
 * json传输时候的键
 */
public final class JSONKey {
  private JSONKey() {
  }

  /**
   * 返回值
   */
  public static final String CODE = "code";

  /**
   * 失败码
   */
  public static final String FAIL_CODE = "0";
  /**
   * 成功
   */
  public static final String SUCCESS_CODE = "1";
  /**
   * 用户名
   */
  public static final String USER_NAME = "userName";

  /**
   * 登录密码
   */
  public static final String USER_PASSWORD = "password";

  /**
   * 上次登陆时间
   */
  public static  final  String LAST_LOGIN = "lastLogin";

  /**
   * 登陆token
   */
  public static  final  String TOKEN = "token";

  /**
   * 消息
   */
  public static final String MESSAGE = "message";

  /**
   * 返回的列表长度
   */
  public static final String LIST_SIZE = "size";

  /**
   * 返回的历史消息
   */
  public static final String HISTORY_LIST = "histories";

  /**
   * 收到消息返回
   */
  public static final String MESSAGE_READ = "message_READ";

  /**
   * 状态原因
   */
  public static final String REASON = "reason";

  /**
   * 广播
   */
  public static final String BROADCAST = "broadcast";

  /**
   * 在线状态
   */
  public static final String ONLINE = "online";

}
