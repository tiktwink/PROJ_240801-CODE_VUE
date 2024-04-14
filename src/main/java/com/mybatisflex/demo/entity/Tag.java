package com.mybatisflex.demo.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.handler.GsonTypeHandler;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("tag")
public class Tag {
  
  @Id(keyType = KeyType.Auto)
  private Integer id;
  
  @Column(typeHandler = GsonTypeHandler.class)
  private String[] tags;
}
