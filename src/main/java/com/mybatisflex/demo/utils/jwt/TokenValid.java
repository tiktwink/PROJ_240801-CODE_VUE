package com.mybatisflex.demo.utils.jwt;


import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class TokenValid { //解析令牌的pojo
  
  Boolean valid = false; //令牌是否有效(true有效，false无效)
  String msg; //令牌是否有效，如果无效，无效的原因是什么
}
