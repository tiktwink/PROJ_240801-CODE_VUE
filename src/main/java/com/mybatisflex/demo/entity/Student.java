package com.mybatisflex.demo.entity;

import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("student")
public class Student {
  
  @Id(keyType = KeyType.None)
  private String id;
  
  private String name;
  private Integer grade;
  private Integer gender;
  private String classId;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date birth;
  @DateTimeFormat(pattern = "yyyy-MM-dd")
  private Date entryTime;
  private String majorId;
  private String majorFlowId;
  
  
  
}
