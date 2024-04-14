package com.mybatisflex.demo.controller;

import com.mybatisflex.demo.entity.Tag;
import com.mybatisflex.demo.mapper.TagMapper;
import com.mybatisflex.demo.entity.table.TagTableDef;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/tag")
public class TagController {
  @Autowired
  TagMapper tagMapper;
  
  @GetMapping("/{id}")
  public Result queryTag(@PathVariable Integer id) {
    
    Tag tag = tagMapper.selectOneById(id);
    return Result.success("查询成功", tag);
  }
  
  @GetMapping("/all")
  public Result queryAllTags() {
    
    List<Tag> tags = tagMapper.selectAll();
    
    return Result.success("查询成功", tags);
  }
  
  @PutMapping("/index")
  public Result addTag(@RequestBody Tag tag) {
    
    long c = QueryChain.of(Tag.class)
                 .select(TagTableDef.TAG.ID)
                 .from(TagTableDef.TAG)
                 .count();
    
    if (c >= 1000) {
      log.info("已存在1000个标签，不能再添加新的");
      return Result.fail("已存在1000个标签，不能再添加新的");
    }
    
    //可以继续添加
    System.out.println("tag tobe added: " + tag);
    
    int i = tagMapper.insert(tag);
    if (i > 0) return Result.success("success");
    else return Result.fail("fail");
  }
  
  @PostMapping("/{id}")
  public Result updateTag(@RequestBody Tag tag) {
    
    int i = tagMapper.update(tag);
    if (i > 0) return Result.success("success");
    else return Result.fail("fail");
  }
  
  
}
