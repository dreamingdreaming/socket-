package com.ls.g4.util;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

/**
 * JWT工具类，用于生产和解析token
 */
public final  class JWTUtil {
  private JWTUtil(){}

  /**
   * 密钥
   */
  private static final String SECRET = "tiantianliantiantiantiankuaile";

  /**
   * 生成token
   * @param userName
   * @return token
   * @throws UnsupportedEncodingException
   */
  public  static  String createToken(String userName) throws UnsupportedEncodingException {
    Map<String, Object> head = new HashMap<>();
    head.put("alg", "HS256");
    head.put("typ", "JWT");
    Algorithm algorithm = Algorithm.HMAC256(SECRET);
    long exp = System.currentTimeMillis()+ TimeUnit.DAYS.toMillis(3);
    String token = JWT.create()
        .withHeader(head)
        .withClaim("userName",userName)
        .withSubject("login")
        .withIssuedAt(new Date())
        .withExpiresAt(new Date(exp))
        .sign(algorithm);
    return token;
  }

  /**
   * 解析token
   * @param token
   * @return username
   * @throws Exception token过期
   */
  public static String praseToken(String token) throws Exception {
    JWTVerifier verifier = null;
    try {
      verifier = JWT.require(Algorithm.HMAC256(SECRET)).build();
      DecodedJWT jwt = verifier.verify(token);
      return jwt.getClaim("userName").asString();
    }  catch (Exception e){
      throw  e;
    }
  }
}
