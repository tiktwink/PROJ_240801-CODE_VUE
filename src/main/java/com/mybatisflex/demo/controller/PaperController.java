package com.mybatisflex.demo.controller;

import com.mybatisflex.demo.serviceimpl.PaperServiceImpl;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Objects;

@Slf4j
@RestController
@RequestMapping("/paper")
public class PaperController {
  
  @Autowired
  PaperServiceImpl paperService;
  
  @Value("${file.upload-path}")
  private String imgUrl;
  
  @GetMapping("/index")
  public Result queryOne(String id){
    
    return paperService.queryOne(id);
  }
  
  @GetMapping("/indexs")
  public Result queryMul(String id, String examId, String studentId, String studentName) {
    //忽略鉴权
    if (!Objects.equals(id, "") && id != null) {
      return paperService.queryOne(id);
    } else {
//      System.out.println("mul select");
      return paperService.queryMul(examId, studentId, studentName);
    }
    
  }
  //通过考试ID查询未导入的试卷
  @GetMapping("/unimportedBL")
  public Result queryUnimportedBL(String examId){
    
    return paperService.queryUnimportedBL(examId);
  }
  
  @PostMapping("/singleupload/{paperId}")
  public Result singleUpload(@RequestBody MultipartFile file, @PathVariable String paperId) {
//    String originalFilename = file.getOriginalFilename();
//    int i = originalFilename.indexOf("-");
//    int j = originalFilename.indexOf("-", i + 1);
//    String paperId = originalFilename.substring(0, i);
//    String examId = originalFilename.substring(i + 1, j);
//    String studentId = originalFilename.substring(j + 1);
//    System.out.println("试卷ID：" + paperId + " 考试ID：" + examId + " 学生ID：" + studentId);
    
   return paperService.singleUpload(paperId, file);
  }
  
  @PostMapping("/multiupload/{examId}")
  public Result multiUpload(@RequestBody MultipartFile file, @PathVariable String examId) throws IOException {
    
    return paperService.multiUpload(examId, file);
  }
  
  @GetMapping("/DS")
  public Result queryDS(String paperId){
    
    return paperService.queryDS(paperId);
  }
  
}
