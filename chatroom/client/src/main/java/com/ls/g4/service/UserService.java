package com.ls.g4.service;

import com.ls.g4.po.User;

public interface UserService {
  /**
   * 登录
   *
   * @param user
   */
  void login(User user);

  /**
   * 获取登录用户
   *
   * @return 登录用户
   */
  User getLoginUser();

  /**
   * 保存token
   * @param token
   */
  void setToken(String token);

  /**
   * 获取token
   * @return token
   */
  String getToken();
}
