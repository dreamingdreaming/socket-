package com.ls.g4.service.impl;

import com.ls.g4.po.User;
import com.ls.g4.service.UserService;

/**
 * @date 2019/8/27
 */
public class UserServiceImpl implements UserService {

  private User user;

  private String token;

  public UserServiceImpl() {

  }

  @Override public void login(User user) {
    this.user = user;
  }

  @Override public User getLoginUser() {
    return user;
  }

  @Override public void setToken(String token) {
    this.token = token;
  }

  @Override public String getToken() {
    return token;
  }

}
