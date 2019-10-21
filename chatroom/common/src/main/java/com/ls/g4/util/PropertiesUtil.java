package com.ls.g4.util;

import java.io.FileInputStream;
import java.io.IOException;
import java.util.Properties;


public final class PropertiesUtil {
  private final static String ROOT_PATH = System.getProperty("user.dir");
  private PropertiesUtil(){}
  public static Properties readOutsideProperties(String url) throws IOException {
    Properties properties = new Properties();
    properties.load(new FileInputStream(ROOT_PATH+url));
    return properties;
  }
}
