package com.mybatisflex.demo.entity.vo;

//import com.entity.demo.aichecksystem.Novel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NovelVo {
  
  //Novel的字段
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
  private Date updateTime;
  private Date createTime;
  
  //元素为VolumeVo
//  private List<VolumeVo> volumes;
  
  //附加字段(user表)
  private String authorNick;
}
