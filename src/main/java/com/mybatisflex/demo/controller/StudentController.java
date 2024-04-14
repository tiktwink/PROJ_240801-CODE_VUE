package com.mybatisflex.demo.controller;

import com.mybatisflex.demo.entity.Student;
import com.mybatisflex.demo.serviceimpl.StudentServiceImpl;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/student")
public class StudentController {
  @Autowired
  StudentServiceImpl studentService;
  
  @PostMapping("/index")
  public Result addStudent(@RequestBody Student student){
    //忽略用户鉴权
    
    return studentService.addStudent(student);
  }
  
  @GetMapping("/index")
  public Result queryStudent(String id, String name, String classId, String grade){
    //忽略鉴权
    
    return studentService.queryStudent(id, name, classId, grade);
  }
  
}
