package com.mybatisflex.demo.entity;

import com.mybatisflex.annotation.Column;
import com.mybatisflex.annotation.Id;
import com.mybatisflex.annotation.KeyType;
import com.mybatisflex.annotation.Table;
import com.mybatisflex.core.keygen.KeyGenerators;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Table("novel")
public class Novel {
  
  @Id(keyType = KeyType.Generator, value = KeyGenerators.flexId)
  private String id;
  
  private String title;
  private String authorId;
  private Integer volumeCount;
  private Integer chapterCount;
  private Integer status;
  private String note;
  private Integer likes;
  private Integer follows;
  private Integer hits;
  private Integer type;
  private Double rate;
  private Integer rateCount;
  private Integer wordCount;
  
  
  
  @Column(onInsertValue = "now()")
  private Date updateTime;
  
  @Column(onInsertValue = "now()")
  private Date createTime;
}
