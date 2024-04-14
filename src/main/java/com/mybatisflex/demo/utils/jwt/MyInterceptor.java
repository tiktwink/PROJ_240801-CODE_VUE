package com.mybatisflex.demo.utils.jwt;

import com.mybatisflex.demo.entity.User;
import com.mybatisflex.demo.mapper.UserMapper;
import com.mybatisflex.demo.utils.Md5Util;
import com.mybatisflex.demo.utils.Result;
import com.mybatisflex.demo.entity.table.UserTableDef;
import com.google.gson.Gson;
import com.mybatisflex.core.mask.MaskManager;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerInterceptor;

import java.io.PrintWriter;


@Slf4j
public class MyInterceptor implements HandlerInterceptor {
  @Autowired
  UserMapper userMapper;
  
  @Override
  public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
    log.info("拦截器生效，拦截路径：" + request.getServletPath());
    response.setCharacterEncoding("utf-8"); //防止中文乱码
    
    String token = request.getHeader("token"); //从请求头中获取token字段值
    String username = request.getHeader("username"); //从请求头中获取username字段值
    
    //如果token不存在
    if (token == null) {
      try (PrintWriter pw = response.getWriter()) {
        Gson gson = new Gson();
        //令牌不存在，直接在拦截器中处理当前请求，返回令牌不存在的Result对象
        String resultJson = gson.toJson(Result.fail("令牌未携带"));
        pw.write(resultJson);
        return false;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    if (username == null) {
      try (PrintWriter pw = response.getWriter()) {
        Gson gson = new Gson();
        //令牌不存在，直接在拦截器中处理当前请求，返回令牌不存在的Result对象
        String resultJson = gson.toJson(Result.fail("用户名未携带"));
        pw.write(resultJson);
        return false;
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
    
    //如果token和username都存在，则验证之
    //验证分两步，1：验证token是否有效(未过期)，2：验证token是不是当前用户的(不能随便拿一个未过期token来增删改其他用户的数据)
    
    //步骤1
    TokenValid tokenValid = TokenUtil.verify(token);
    if (!tokenValid.getValid()) {
      
      try (PrintWriter pw = response.getWriter()) {
        Gson gson = new Gson();
        //令牌无效，直接在拦截器中处理当前请求，返回令牌无效的Result对象
        String resultJson = gson.toJson(Result.fail(tokenValid.getMsg()));
        pw.write(resultJson);
        return false;
      } catch (Exception e) {
        System.out.println("error in tokenvalid test...");
        e.printStackTrace();
      }
    }
    
    //步骤2
    try {
      MaskManager.skipMask(); //暂停数据脱敏
      
      User user = userMapper.selectOneByCondition(UserTableDef.USER.USERNAME.eq(username));
      if (user == null) {
        try (PrintWriter pw = response.getWriter()) {
          Gson gson = new Gson();
          //令牌无效，直接在拦截器中处理当前请求，返回令牌无效的Result对象
          String resultJson = gson.toJson(Result.fail("用户名不存在"));
          pw.write(resultJson);
          return false;
        } catch (Exception e) {
          System.out.println("error in username query...");
          e.printStackTrace();
        }
      } else {
        String md5_token = user.getToken();
        if (!Md5Util.checkPassword(token, md5_token)) {
          try (PrintWriter pw = response.getWriter()) {
            Gson gson = new Gson();
            //令牌无效，直接在拦截器中处理当前请求，返回令牌无效的Result对象
            String resultJson = gson.toJson(Result.fail("token值不匹配"));
            pw.write(resultJson);
            return false;
          } catch (Exception e) {
            System.out.println("error in token check...");
            e.printStackTrace();
          }
        }
        
      }
      
      log.info("当前账号: " + username + " 携带的token有效、合法，拦截器端将放行，如需鉴权，将进一步处理...");
      
      return true;
      
    } finally {
      MaskManager.restoreMask(); //恢复数据脱敏
    }
  }
  
  
}
