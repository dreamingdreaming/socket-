package com.ls.g4.service.impl;

import com.ls.g4.cache.UserCache;
import com.ls.g4.mapper.UserDao;
import com.ls.g4.po.User;
import com.ls.g4.server.ServerHandler;
import com.ls.g4.service.UserService;
import com.ls.g4.util.SqlSessionUtil;

import java.util.Map;

public class UserServiceImpl implements UserService {
  UserDao userDao = SqlSessionUtil.getSqlSession().getMapper(UserDao.class);
  UserCache userCache = new UserCache();

  @Override public long login(String userName, String password, ServerHandler handler) {
    User user = new User();
    user.setName(userName);
    user.setPwd(password);
    User loginUser = userDao.queryUser(user);
    if (loginUser != null) {
      userCache.putUser(userName, handler);
      userDao.updateLoginTime(loginUser.getId(),System.currentTimeMillis());
      return loginUser.getLoginTime();
    }
    return -1;
  }

  @Override public boolean isOnline(User user) {
    Map<String, ServerHandler> allUser = userCache.getAllUser();
    for (String userName : allUser.keySet()) {
      if (userName.equals(user.getName())) {
        return true;
      }
    }
    return false;
  }

  @Override public void logout(String userName) {
    userCache.removeUser(userName);
  }

  @Override public boolean exist(User user) {
    return false;
  }

  @Override public UserCache getUserCache() {
    return userCache;
  }
}
