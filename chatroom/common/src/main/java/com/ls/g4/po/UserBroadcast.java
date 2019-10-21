package com.ls.g4.po;

/**
 * 用户上下线广播
 */
public class UserBroadcast {
  private String userName;
  private boolean state;

  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  public boolean isState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }
}
