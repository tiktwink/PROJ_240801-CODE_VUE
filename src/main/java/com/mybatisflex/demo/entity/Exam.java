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
@Table("exam")
public class Exam {
  
  @Id(keyType = KeyType.None)
  private String id;
  
  private Integer checked;
  private Integer doubleChecked;
  private String name;
  private String title;
  private String subjectId;
  private String subjectName;
  private String examPos;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date examTime;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date planCheckTime;
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date checkTime;
  private String checkCode;
  private String testCode;
  
}
