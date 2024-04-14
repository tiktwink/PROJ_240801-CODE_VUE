package com.mybatisflex.demo.controller;


import com.mybatisflex.demo.serviceimpl.SubjectServiceImpl;
import com.mybatisflex.demo.utils.Result;
import com.mybatisflex.demo.entity.Subject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@Slf4j
@RequestMapping("/subject")
public class SubjectController {
  
  @Autowired
  SubjectServiceImpl subjectService;
  
  
  @GetMapping("/queryall")
  public Result queryAll(){
    //省略用户鉴权
    
    return subjectService.queryAll();
  }
  
  @PutMapping("/index")
  public Result addSubject(@RequestBody Subject subject){
    //省略用户鉴权
    
    return subjectService.add(subject);
  }
  
  @DeleteMapping("/{id}")
  public Result deleteSubject(@PathVariable String id){
    //省略用户鉴权
    
    return subjectService.delete(id);
  }
}
