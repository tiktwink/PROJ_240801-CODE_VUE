package com.mybatisflex.demo.utils;

import com.mybatisflex.demo.entity.Novel;
import com.mybatisflex.demo.entity.User;
import com.mybatisflex.demo.mapper.NovelMapper;
import com.mybatisflex.demo.mapper.UserMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class AuthenUtil {
  @Autowired
  UserMapper userMapper;
  @Autowired
  NovelMapper novelMapper;
  
  public Boolean hasAuthenOnNovel(String username, String novelId) {
    
    User user = userMapper.selectOneById(username);
    Novel novel = novelMapper.selectOneById(novelId);
    
    if (user == null || novel == null) {
      return false;
    }
    if (user.getUsername().length() < 8) {
      return true;
    } else return user.getUsername().equals(novel.getAuthorId());
  }
}
