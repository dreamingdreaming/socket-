package com.ls.g4.service;

import com.ls.g4.cache.UserCache;
import com.ls.g4.po.User;
import com.ls.g4.server.ServerHandler;

/**
 * 用户业务层接口
 */
public interface UserService {
  /**
   * 用户登录
   *
   * @param userName 用户名
   * @param password 密码
   * @param handler  serverHandlerd用于缓存
   * @return 登录时间 <code>-1</code>登陆失败
   */
  long login(String userName, String password, ServerHandler handler);

  /**
   * 用户是否在线
   *
   * @param user
   * @return
   */
  boolean isOnline(User user);

  /**
   * 用户登出
   *
   * @param userName
   */
  void logout(String userName);

  /**
   * 判断用户是否存在
   *
   * @param user
   * @return
   */
  boolean exist(User user);

  /**
   * 获取用户缓存
   *
   * @return
   */
  UserCache getUserCache();
}
