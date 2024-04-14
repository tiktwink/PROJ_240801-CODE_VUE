package com.mybatisflex.demo;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
@MapperScan("com.mybatisflex.demo.mapper")
public class CheckApplication {
  
  public static void main(String[] args) {
    
    SpringApplication.run(CheckApplication.class, args);
    
  }
  
}
