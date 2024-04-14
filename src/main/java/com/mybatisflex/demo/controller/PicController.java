package com.mybatisflex.demo.controller;
//import io.swagger.annotations.ApiOperation;
import com.mybatisflex.demo.utils.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;


@RestController
public class PicController {
  @Value("${file.upload-path}")
  private String imgUrl;
  
  @PostMapping("/upload")
  public Result returnImg(@RequestBody MultipartFile file) {
    String originalFilename = file.getOriginalFilename();
//    int index = originalFilename.indexOf(".");
    String formatFileName = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyy-MM-dd-HH-mm")).toString();
//    String newFileName = formatFileName + originalFilename.substring(index);
    String newFileName = formatFileName + "+" + originalFilename;
    
    try {
      //将文件保存指定目录
      file.transferTo(new File(imgUrl + newFileName));
      return Result.success("图片传输成功", "localhost:8080/fwwb202403/"+newFileName);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Result.fail("图片传输出现了错误！");
  }
}
