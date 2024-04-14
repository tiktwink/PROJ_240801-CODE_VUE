package com.mybatisflex.demo.utils.jwt;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class Token { //下发令牌的pojo
  
  private String tokenValue;
  private String validDt = null; //token有效期
  private String generateDt = null; //token生成期
}
