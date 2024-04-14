package com.mybatisflex.demo.controller;

import com.mybatisflex.demo.entity.User;
import com.mybatisflex.demo.serviceimpl.UserServiceimpl;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;


@RestController
@Slf4j
@RequestMapping("/user")
public class UserController {
  
  @Autowired
  UserServiceimpl userServiceimpl;
  
  @PostMapping("/signin")
  public Result signin(@RequestBody User user) {
    return userServiceimpl.signin(user);
  }
  
  @PostMapping("/login")
  public Result login(@RequestBody User user) {
    return userServiceimpl.login(user);
  }
  
  @GetMapping("/test")
  public Result test() {
    
    return Result.success("test");
  }
  
}
