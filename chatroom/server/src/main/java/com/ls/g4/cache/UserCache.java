package com.ls.g4.cache;

import com.ls.g4.server.ServerHandler;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * 在线用户缓存
 */
public class UserCache {

  private Map<String, ServerHandler> users = new ConcurrentHashMap<String, ServerHandler>();

  public void putUser(String user, ServerHandler thread) {
    users.put(user, thread);
  }

  public void removeUser(String user) {
    users.remove(user);
  }

  public ServerHandler getServerThread(String user) {
    return users.get(user);
  }

  public Map<String, ServerHandler> getAllUser() {
    Map<String, ServerHandler> backs = new HashMap<String, ServerHandler>();
    backs.putAll(users);
    return backs;
  }
}
