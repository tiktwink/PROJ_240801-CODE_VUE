package com.mybatisflex.demo.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class PaperVo {
  //Paper的字段
  private String id;
  private String examId;
  private String studentId;
  private Integer checked;
  private Integer doubleChecked;
  
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date createTime;
  
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date test_time;
  private Integer tested;
  
  @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
  private Date checkTime;
  
  private String url;
  private Integer hasUrl;
  
  private Integer judge;
  private Integer baseScore;
  private Integer judgeScore;
  
  
  //Student的字段
  private String studentName;
}
