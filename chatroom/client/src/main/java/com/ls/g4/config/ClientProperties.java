package com.ls.g4.config;

import com.ls.g4.util.PropertiesUtil;

import java.io.IOException;
import java.util.Properties;

/**
 * @date 2019/9/3
 */
public final class ClientProperties {
  private ClientProperties(){}

  private final static String URL = "/config/client.properties";
  private static Properties  properties = null;
  private static final int PORT_DEFAULT = 9999;
  private static final String HOST_DEFAULT = "127.0.0.1";
  private static final String HOST_KEY = "host";
  private static final String PORT_KEY = "port";

  /**
   * 编码格式
   */
  public static final String CHARSET = "utf-8";

  /**
   * 端口号
   */
  public static int port;

  /**
   * 主机名
   */
  public static String host;

  static {
    try {
      properties = PropertiesUtil.readOutsideProperties(URL);
      port = Integer.parseInt(properties.getProperty(PORT_KEY,String.valueOf(PORT_DEFAULT)));
      host = properties.getProperty(HOST_KEY,HOST_DEFAULT);
    } catch (IOException e) {
      System.out.println("本地系统文件读取错误....采用默认系统文件");
    }
  }
}
