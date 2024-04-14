package com.mybatisflex.demo.serviceimpl;

import com.mybatisflex.demo.entity.User;
import com.mybatisflex.demo.utils.jwt.Token;
import com.mybatisflex.demo.utils.jwt.TokenUtil;
import com.mybatisflex.core.mask.MaskManager;
import com.mybatisflex.demo.mapper.UserMapper;
import com.mybatisflex.demo.utils.Md5Util;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;

@Slf4j
@Service
public class UserServiceimpl {
  @Autowired
  UserMapper userMapper;
  
  public Result signin(User user) {
    log.info("欲注册账户名: " + user.getUsername());
    
    User userFound = userMapper.selectOneById(user.getUsername());
    if (userFound != null) {
      return Result.fail("用户名已存在");
    }
    user.setTokenGenerateDt(null); //账户注册时，不下发token，阻止自动生成的令牌生成期
    user.setPassword(Md5Util.getMD5String(user.getPassword())); //后台md5转换
    log.info("拟注册账户名: " + user.getUsername());
    
    int i = userMapper.insert(user); //不忽略null值新增
    if (i > 0) {
      log.info("注册账户成功");
      log.info("账户名: " + user.getUsername());
      return Result.success("注册账户成功");
    } else {
      log.info("注册账户失败");
      return Result.fail("注册账户失败，请咨询管理员");
    }
  }
  
  
  public Result login(User user) {
    log.info("欲登录账户: " + user.getUsername());

//    User userFound = userMapper.selectOneById(user.getUsername());
    User userFound;
    try {
      MaskManager.skipMask(); //跳过数据脱敏，核验密码
      userFound = userMapper.selectOneById(user.getUsername());
      if (userFound == null) {
        log.info("不存在的用户名欲登录，登录请求驳回");
        log.info("欲登录账户: " + user.getUsername());
        return Result.fail("用户名不存在");
      } else {
        String md5_password = userFound.getPassword();
        String password = user.getPassword();
        if (!Md5Util.checkPassword(password, md5_password)) {
          log.info("用户密码错误，登录请求驳回");
          log.info("欲登录账户: " + user.getUsername());
          return Result.fail("密码错误");
        }
        log.info(user.getPassword() + " 登录成功，将下发令牌...");
        //下发令牌
        String tokenValue = TokenUtil.getToken(user);
        String tokenGenerateDt = (new Date()).toString();
        String tokenValidDt = (new Date(System.currentTimeMillis() + 30L * 24 * 3600 * 1000)).toString();
        //将token值更新到数据库user表中
        User user1 = new User();
        user1.setUsername(user.getUsername());
        user1.setToken(Md5Util.getMD5String(tokenValue));
        int i = userMapper.update(user1); //update默认忽略null值，只更新非null字段
        if (i == 0) {
          log.info("token存取错误，登录请求失败");
          log.info("欲登录信息: " + user);
          log.info("token值: " + tokenValue + " token更新时间: " + tokenGenerateDt);
          return Result.fail("登录失败，token存取错误");
        }
        
        Token token = new Token();
        token.setTokenValue(tokenValue);
        token.setGenerateDt(tokenGenerateDt);
        token.setValidDt(tokenValidDt);
        
        log.info("令牌下发，将返回响应!");
        return Result.success("登录成功", token);
      }
    } finally { //恢复数据脱敏
      MaskManager.restoreMask();
    }
    
    
  }
  
  
}
