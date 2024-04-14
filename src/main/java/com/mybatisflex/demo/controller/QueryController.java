package com.mybatisflex.demo.controller;

import com.mybatisflex.demo.utils.AuthenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/query")
public class QueryController {
  
  @Autowired
  AuthenUtil authenUtil;
  
  
}
