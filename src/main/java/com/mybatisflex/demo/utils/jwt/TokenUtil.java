package com.mybatisflex.demo.utils.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.mybatisflex.demo.entity.User;
import lombok.extern.slf4j.Slf4j;


import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
public class TokenUtil { //令牌的生成和验证
  
  private static final String SIGN = "!^&%&*!@$*%!!@(&%2ar^2t";
  
  /**
   * 生成Token
   */
  public static String getToken(User user) {
    JWTCreator.Builder builder = JWT.create();
    
    //配置令牌有效期
    Calendar instance = Calendar.getInstance();
    instance.add(Calendar.DATE,30);//过期时间为30天
    //instance.add(Calendar.MINUTE, 1);//过期时间为1分钟(用于测试token过期时拦截器是否能够正确处理)
    
    Map<String, String> map = new HashMap<>();
    //这里应该用从数据库中查到的User对象user.getUsername()来代替"zhangsan"
    map.put("username", user.getUsername());
    //这里应该用从数据库中查到的User对象user.getPassword()来代替"123456"
    map.put("password", user.getPassword());
    //将时间戳加入，使每次生成的token与之前的不同
    map.put("generateDT", (new Date()).toString());
    
    map.forEach(builder::withClaim);
    return builder.withExpiresAt(instance.getTime()).sign(Algorithm.HMAC256(SIGN));
  }
  
  /**
   * 验证token
   */
  public static TokenValid verify(String token) {
    try {
      //尝试解析令牌
      DecodedJWT verify = JWT.require(Algorithm.HMAC256(SIGN)).build().verify(token);
//            Map<String, Claim> claims = verify.getClaims(); //获取所有携带数据
//            System.out.println(claims);
//            System.out.println("用户名：" + verify.getClaim("username"));
//            System.out.println("密码：" + verify.getClaim("password"));
//            System.out.println("token生成时间：" + verify.getClaim("generateDT"));
//            System.out.println("token过期时间：" + verify.getExpiresAt()); //token过期时间
      
      log.info("令牌有效");
      TokenValid tokenValid = new TokenValid();
      tokenValid.setValid(true);
      tokenValid.setMsg("令牌有效");
      return tokenValid;
    }catch (TokenExpiredException e){
      log.info("令牌过期");
      TokenValid tokenValid = new TokenValid();
      tokenValid.setValid(false);
      tokenValid.setMsg("令牌过期");
      return tokenValid;
    }catch (Exception e){
      log.info("令牌无效");
      TokenValid tokenValid = new TokenValid();
      tokenValid.setValid(false);
      tokenValid.setMsg("令牌无效");
      return tokenValid;
    }
    
    
  }
  
  
}
