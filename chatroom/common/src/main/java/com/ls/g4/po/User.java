package com.ls.g4.po;

import com.ls.g4.util.DateUtil;

import java.io.Serializable;

public class User implements Serializable {
  /**
   *
   */
  private static final long serialVersionUID = 1L;
  private int id;
  private String name;
  private String pwd;
  private boolean state;
  private Long loginTime;

  public User(int id, String name, String pwd, boolean state, Long loginTime) {
    this.id = id;
    this.name = name;
    this.pwd = pwd;
    this.state = state;
    this.loginTime = loginTime;
  }

  public User() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getPwd() {
    return pwd;
  }

  public void setPwd(String pwd) {
    this.pwd = pwd;
  }

  public boolean isState() {
    return state;
  }

  public void setState(boolean state) {
    this.state = state;
  }

  public Long getLoginTime() {
    return loginTime;
  }

  public void setLoginTime(Long loginTime) {
    this.loginTime = loginTime;
  }

  @Override
  public String toString() {
    return "User [id=" + id + ", name=" + name + ", pwd=" + pwd + ", state=" + state + ", time="
        + DateUtil.long2DateString(loginTime) + "]";
  }

}
