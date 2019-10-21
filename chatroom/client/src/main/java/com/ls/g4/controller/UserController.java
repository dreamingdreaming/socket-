package com.ls.g4.controller;

import com.alibaba.fastjson.JSONObject;
import com.ls.g4.client.Client;
import com.ls.g4.config.ClientProperties;
import com.ls.g4.po.User;
import com.ls.g4.protocol.MsgProtocolV1;
import com.ls.g4.protocol.MsgProtocolV1.DataPacket;
import com.ls.g4.protocol.MsgType;
import com.ls.g4.service.UserService;
import com.ls.g4.service.impl.UserServiceImpl;
import com.ls.g4.util.DateUtil;
import com.ls.g4.util.JSONKey;
import com.ls.g4.view.View;

import java.io.IOException;

/**
 * 用户模块控制层
 */
public class UserController {

  private Client client;

  public UserService userService;

  private View view;

  public UserController(Client client) {
    this.client = client;
    this.view = client.getView();
    userService = new UserServiceImpl();
  }

  public void login() throws IOException {
    boolean result = false;
    do {
      try {
        view.show("请输入用户名: ");
        String userName = view.input();
        view.show("请输入密码: ");
        String password = view.input();
        JSONObject data = new JSONObject();
        data.put(JSONKey.USER_NAME, userName);
        data.put(JSONKey.USER_PASSWORD, password);
        DataPacket dataPacket = MsgProtocolV1.protocol.new DataPacket();
        dataPacket.setMsgType(MsgType.LOGIN.getType()).setData(data.toJSONString().getBytes(
            ClientProperties.CHARSET));
        client.getClientHandler().write(dataPacket);
        DataPacket backDatapacket = MsgProtocolV1.protocol.readDataPacketFromStream(client.getIn());
        JSONObject back =  JSONObject.parseObject(new String(backDatapacket.getData()
            ,ClientProperties.CHARSET));
        if (JSONKey.SUCCESS_CODE.equals(back.get(JSONKey.CODE))) {
          result = true;
          String token = back.getString(JSONKey.TOKEN);
          User user = new User();
          user.setName(userName);
          userService.login(user);
          userService.setToken(token);
          view.show("登录成功！欢迎你，" + user.getName());
          view.show("上次登录时间 ：" + DateUtil.long2DateString(back.getLong(JSONKey.LAST_LOGIN)));
        } else {
          view.show("登录失败！"+ back.getString(JSONKey.REASON));
        }
        // 返回结果
      } catch (IOException e) {
        throw e;
      }
    } while (!result);
  }

  public boolean reLogin() throws IOException {
    JSONObject data = new JSONObject();
    data.put(JSONKey.TOKEN,userService.getToken());
    MsgProtocolV1.DataPacket dataPacket = MsgProtocolV1.protocol.new DataPacket()
        .setMsgType(MsgType.LOGIN.getType()).setData(data.toJSONString().getBytes(ClientProperties.CHARSET));
    client.getClientHandler().write(dataPacket);
    MsgProtocolV1.DataPacket backDataPacket = MsgProtocolV1.protocol.readDataPacketFromStream(client.getIn());
    JSONObject back = JSONObject.parseObject(new String(backDataPacket.getData(),ClientProperties.CHARSET));
    return JSONKey.SUCCESS_CODE.equals(back.get(JSONKey.CODE));
  }

  public void logOut() {
    DataPacket dataPacket = MsgProtocolV1.protocol.new DataPacket();
    dataPacket.setMsgType(MsgType.LOGOUT.getType());
    client.getClientHandler().write(dataPacket);
  }
}
