package com.ls.g4.config;

import com.ls.g4.util.PropertiesUtil;

import java.io.IOException;
import java.util.Properties;

public final class ServerProperties {
  private ServerProperties(){}

  private final static String URL = "/config/server.properties";
  private static Properties  properties = null;
  private static final int DEFAULT_PORT = 9999;
  private static final int DEFAULT_TIMEOUT = 5;
  private static final int DEFAULT_MAX_ONLINE = 50;
  private static final String PORT_KEY = "port";
  private static final String TIME_OUT_KEY = "timeOut";
  private static final String MAX_ONLINE_KEY = "maxOnline";

  /**
   * 编码格式
   */
  public static final String CHARSET = "utf-8";
  /**
   * 服务器读端口号
   */
  public static int port;
  /**
   * 断线重连 超时时间
   */
  public static int timeOut;
  /**
   * 最大连接数
   */
  public static int maxOnline;

  static{
    try {
      properties = PropertiesUtil.readOutsideProperties(URL);
      port = Integer.parseInt(properties.getProperty(PORT_KEY,String.valueOf(DEFAULT_PORT)));
      timeOut = Integer.parseInt(properties.getProperty(TIME_OUT_KEY,String.valueOf(DEFAULT_TIMEOUT)));
      maxOnline = Integer.parseInt(properties.getProperty(MAX_ONLINE_KEY,String.valueOf(DEFAULT_MAX_ONLINE)));
    } catch (IOException e) {
      System.out.println("系统文件读取错误....采用默认系统文件配置");
    }
  }
}
