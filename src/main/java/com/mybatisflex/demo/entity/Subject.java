package com.mybatisflex.demo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Table("subject")
public class Subject {
  
  @Id(keyType = KeyType.None)
  private String id;
  
  private String title;
  private String type;
  private Integer sugsGrade;
  private Integer num;
  private String note;
}
