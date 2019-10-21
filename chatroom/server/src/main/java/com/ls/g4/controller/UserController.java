package com.ls.g4.controller;

import com.alibaba.fastjson.JSONObject;
import com.ls.g4.cache.UserCache;
import com.ls.g4.config.ServerProperties;
import com.ls.g4.po.User;
import com.ls.g4.po.UserBroadcast;
import com.ls.g4.protocol.MsgProtocolV1;
import com.ls.g4.protocol.MsgProtocolV1.DataPacket;
import com.ls.g4.protocol.MsgType;
import com.ls.g4.server.ServerHandler;
import com.ls.g4.service.UserService;
import com.ls.g4.service.impl.UserServiceImpl;
import com.ls.g4.util.JSONKey;
import com.ls.g4.util.JWTUtil;

import java.io.IOException;

public class UserController {

  UserService userService = new UserServiceImpl();

  /**
   * 用户登录
   *
   * @throws IOException
   */
  public void login(JSONObject data, ServerHandler serverHandler) throws IOException {
    String userName = data.getString(JSONKey.USER_NAME);
    String password = data.getString(JSONKey.USER_PASSWORD);
    String token = data.getString(JSONKey.TOKEN);
    JSONObject back = new JSONObject();
    boolean broadcast = false;
    if (token != null) {
      try {
        String tokenUserName = JWTUtil.praseToken(token);
        userName = tokenUserName;
        ServerHandler oldHandler = userService.getUserCache().getAllUser().get(tokenUserName);
        if (oldHandler != null) {
          oldHandler.reconnect();
        } else {
          broadcast = true;
        }
        User login = new User();
        login.setName(tokenUserName);
        serverHandler.setUser(login);
        userService.getUserCache().putUser(tokenUserName, serverHandler);
        back.put(JSONKey.CODE, JSONKey.SUCCESS_CODE);
      } catch (Exception e) {
        back.put(JSONKey.CODE, JSONKey.FAIL_CODE);
        back.put(JSONKey.REASON, "token过期，请重新登陆");
      }
    } else {
      User user = new User();
      user.setName(userName);
      user.setPwd(password);
      long lastLoginTime = -1;
      if ((lastLoginTime = userService.login(userName, password, serverHandler)) != -1) {
        broadcast = true;
        back.put(JSONKey.CODE, JSONKey.SUCCESS_CODE);
        back.put(JSONKey.LAST_LOGIN, lastLoginTime);
        back.put(JSONKey.TOKEN, JWTUtil.createToken(userName));
        User login = new User();
        login.setName(userName);
        login.setLoginTime(lastLoginTime);
        serverHandler.setUser(login);
      } else {
        back.put(JSONKey.CODE, JSONKey.FAIL_CODE);
        back.put(JSONKey.REASON, "用户名或密码错误");
      }
    }
    DataPacket dataPacket = MsgProtocolV1.protocol.new DataPacket()
        .setMsgType(MsgType.LOGIN_BACK.getType()).setData(back.toJSONString().getBytes(
            ServerProperties.CHARSET));
    serverHandler.getOut().write(dataPacket.toBytes());
    if (broadcast) {
      JSONObject borast = new JSONObject();
      UserBroadcast userBroadcast = new UserBroadcast();
      userBroadcast.setUserName(userName);
      userBroadcast.setState(true);
      borast.put(JSONKey.BROADCAST, userBroadcast);
      serverHandler.write(borast, true);
    }
  }

  /**
   * 用户登出
   */
  public void logout(ServerHandler serverHandler) {
    if (serverHandler.getUser()!=null){
      String userName = serverHandler.getUser().getName();
      userService.logout(userName);
      JSONObject borast = new JSONObject();
      UserBroadcast broadcast = new UserBroadcast();
      broadcast.setUserName(userName);
      broadcast.setState(false);
      borast.put(JSONKey.BROADCAST, broadcast);
      serverHandler.write(borast, true);
      Thread.currentThread().interrupt();
    }
  }

  /**
   * @return 用户缓存
   */
  public UserCache getUserCache() {
    return userService.getUserCache();
  }
}
