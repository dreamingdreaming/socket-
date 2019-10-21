package com.ls.g4.po;

import com.ls.g4.util.DateUtil;

import java.io.Serializable;

public class Message implements Serializable {
  private static final long serialVersionUID = 1L;
  private int id;
  private String senderName;
  private String receiverName;
  private String msg;
  private long time;
  private int status;

  public Message(int id, String senderName, String receiverName, String msg, long time,
      int status) {
    this.id = id;
    this.senderName = senderName;
    this.receiverName = receiverName;
    this.msg = msg;
    this.time = time;
    this.status = status;
  }

  public Message() {
  }

  public int getId() {
    return id;
  }

  public void setId(int id) {
    this.id = id;
  }

  public String getSenderName() {
    return senderName;
  }

  public void setSenderName(String senderName) {
    this.senderName = senderName;
  }

  public String getReceiverName() {
    return receiverName;
  }

  public void setReceiverName(String receiverName) {
    this.receiverName = receiverName;
  }

  public String getMsg() {
    return msg;
  }

  public void setMsg(String msg) {
    this.msg = msg;
  }

  public long getTime() {
    return time;
  }

  public void setTime(long time) {
    this.time = time;
  }

  public int getStatus() {
    return status;
  }

  public void setStatus(int status) {
    this.status = status;
  }

  @Override public String toString() {
    return "Message{" + "id=" + id + ", senderName='" + senderName + '\'' + ", receiverName='"
        + receiverName + '\'' + ", msg='" + msg + '\'' + ", time=" + DateUtil.long2DateString(time) + ", status=" + status
        + '}';
  }
}
