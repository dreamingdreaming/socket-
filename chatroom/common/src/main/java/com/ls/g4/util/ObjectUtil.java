package com.ls.g4.util;

import com.alibaba.fastjson.JSON;
import com.ls.g4.po.Message;
import com.ls.g4.po.User;

import java.util.Map;


public final class ObjectUtil {
  private ObjectUtil() {
  }

  public static byte[] msg2byteArray(Message msg) {
    byte[] data = JSON.toJSONString(msg).getBytes();
    return data;
  }

  public static Message byteArray2Message(byte[] data) {
    Message msg = JSON.parseObject(data, Message.class);
    return msg;
  }

  public static User byteArray2User(byte[] data) {
    User user = JSON.parseObject(data, User.class);
    return user;
  }

  public static byte[] user2byteArray(User user) {
    byte[] data = JSON.toJSONString(user).getBytes();
    return data;
  }

  public static byte[] map2byteArray(Map<String, String> map) {
    String json = JSON.toJSONString(map);
    return json.getBytes();
  }

  public static Map byteArray2Map(byte[] data) {
    return JSON.parseObject(data, Map.class);
  }

}
