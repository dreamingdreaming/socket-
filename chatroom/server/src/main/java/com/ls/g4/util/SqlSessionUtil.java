package com.ls.g4.util;

import org.apache.ibatis.io.Resources;
import org.apache.ibatis.session.SqlSession;
import org.apache.ibatis.session.SqlSessionFactory;
import org.apache.ibatis.session.SqlSessionFactoryBuilder;

import java.io.IOException;
import java.io.InputStream;

public final class SqlSessionUtil {
  static SqlSessionFactory sqlSessionFactory;

  private SqlSessionUtil(){
  }

  private static void factoryInit(){
    //mybatis的配置文件，该文件路径见上图
    String resource = "mybatis_config.xml";
    //加载mybatis的配置文件
    InputStream inputStream = null;
    try {
      inputStream = Resources.getResourceAsStream(resource);
    } catch (IOException e) {
      e.printStackTrace();
    }
    //构建sqlSession的工厂
    sqlSessionFactory = new SqlSessionFactoryBuilder().build(inputStream);
  }

  /**
   * 返回session
   * @return session
   */
  public static final SqlSession getSqlSession(){
    if(null == sqlSessionFactory){
      factoryInit();
    }
    SqlSession session = sqlSessionFactory.openSession(true);
    return session;
  }



}
