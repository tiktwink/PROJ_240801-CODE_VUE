package com.mybatisflex.demo.entity;

import com.mybatisflex.annotation.*;
import com.mybatisflex.core.mask.Masks;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("user")
public class User {

  @Id(keyType = KeyType.None)
  String username;
  
  @ColumnMask(Masks.PASSWORD)
  String password;
  
  String usernick;
  
  String token;
  
  @Column(onUpdateValue = "now()")
  Date tokenGenerateDt;
  
  @Column(onInsertValue = "now()",isLarge = true)
  Date createTime;
}
