package com.mybatisflex.demo.entity.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class ExamVo {
 //Exam字段
  private String id;
  private Integer checked;
  private Integer doubleChecked;
  private String name;
  private String title;
  private String subjectId;
  private String subjectName;
  private String examPos;
  private Date examTime;
  private Date planCheckTime;
  private Date checkTime;
  private String checkCode;
  private String testCode;
  //others
  private Integer num; //报考人数
  private Integer countUntested;
  private Integer countUnchecked;
  private Integer countHasUrl; //已导入答卷数

}
