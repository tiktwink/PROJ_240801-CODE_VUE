package com.mybatisflex.demo.serviceimpl;

import com.mybatisflex.demo.entity.Exam;
import com.mybatisflex.demo.entity.Paper;
import com.mybatisflex.demo.entity.Student;
import com.mybatisflex.demo.mapper.PaperMapper;
import com.mybatisflex.demo.entity.table.ExamTableDef;
import com.mybatisflex.demo.entity.table.PaperTableDef;
import com.mybatisflex.demo.entity.table.StudentTableDef;
import com.mybatisflex.core.audit.AuditManager;
import com.mybatisflex.core.audit.ConsoleMessageCollector;
import com.mybatisflex.core.audit.MessageCollector;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.demo.entity.vo.PaperVo;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Slf4j
@Service
public class PaperServiceImpl {
  
  @Autowired
  PaperMapper paperMapper;
  
  @Value("${file.upload-path}")
  private String imgUrl;
  
  
  public Result queryOne(String id) {
//    System.out.println(id);
    Paper paper = paperMapper.selectOneById(id);
    List<Paper> papers = new ArrayList<>();
    papers.add(paper);
    if (paper != null) return Result.success("查询成功", papers);
    
    return Result.fail("查询失败");
  }
  
  public Result queryMul(String examId, String studentId, String studentName) {
    MessageCollector collector = new ConsoleMessageCollector();
    AuditManager.setMessageCollector(collector);
    AuditManager.setAuditEnable(true);
    
    if (Objects.equals(examId, "")) examId = null;
    if (Objects.equals(studentId, "")) studentId = null;
    
    if (Objects.equals(studentName, "") || studentName==null)
      studentName = null;
    else
      studentName = "%" + studentName + "%";
    
    List<PaperVo> papers = QueryChain.of(Paper.class)
                               .select(PaperTableDef.PAPER.DEFAULT_COLUMNS,
                                   StudentTableDef.STUDENT.NAME.as("studentName"))
                               .from(PaperTableDef.PAPER, StudentTableDef.STUDENT)
                               .where(PaperTableDef.PAPER.STUDENT_ID.eq(StudentTableDef.STUDENT.ID))
                               .and(PaperTableDef.PAPER.EXAM_ID.eq(examId))
                               .and(PaperTableDef.PAPER.STUDENT_ID.eq(studentId))
                               .and(StudentTableDef.STUDENT.NAME.like(studentName))
                               .listAs(PaperVo.class);
    
    if (!papers.isEmpty()) {
      return Result.success("查询成功", papers);
    }

    
    return Result.fail("查询失败");
  }
  
  public Result uploads(String id, MultipartFile file) {
    
    return Result.success("上传成功");
  }
  
  public Result singleUpload(String id, MultipartFile file) {
    
    Exam exam = QueryChain.of(Exam.class)
                    .select(ExamTableDef.EXAM.DEFAULT_COLUMNS)
                    .from(ExamTableDef.EXAM, PaperTableDef.PAPER)
                    .where(PaperTableDef.PAPER.ID.eq(id))
                    .and(ExamTableDef.EXAM.ID.eq(PaperTableDef.PAPER.EXAM_ID))
                    .one();
    if (exam == null) {
      return Result.fail("未查到考试ID");
    }
    
    Student student = QueryChain.of(Student.class)
                          .select(StudentTableDef.STUDENT.DEFAULT_COLUMNS)
                          .from(StudentTableDef.STUDENT, PaperTableDef.PAPER)
                          .where(PaperTableDef.PAPER.ID.eq(id))
                          .and(StudentTableDef.STUDENT.ID.eq(PaperTableDef.PAPER.STUDENT_ID))
                          .one();
    if (student == null) {
      return Result.fail("未查到学生ID");
    }
    
    try {
      //设置统一规范的文件名：试卷号-考试号-学生号.jpg
      String newFileName = id + "-" + exam.getId() + "-" + student.getId() + ".jpg";
      //转存到服本地务器
      file.transferTo(new File(imgUrl + newFileName));
      //更新数据库
      Paper paper = paperMapper.selectOneById(id);
      paper.setUrl("localhost:8080/fwwb202403/" + newFileName);
      paper.setHasUrl(1);
      paperMapper.update(paper);
      
      return Result.success("上传成功", "localhost:8080/fwwb202403/" + newFileName);
    } catch (Exception e) {
      e.printStackTrace();
      
    }
    
    return Result.error("存储异常");
  }
  
  public Result multiUpload(String examId, MultipartFile file) throws IOException {
    try {
      String originalFilename = file.getOriginalFilename();
      int i = originalFilename.indexOf("-");
      String paperId = originalFilename.substring(0, i);
      if (paperId.isEmpty())
        return Result.fail("未查询到相关试卷，可能是答卷命名不规范");
      
      Paper paper = QueryChain.of(paperMapper)
                        .select(PaperTableDef.PAPER.DEFAULT_COLUMNS)
                        .from(PaperTableDef.PAPER)
                        .where(PaperTableDef.PAPER.ID.eq(paperId).and(PaperTableDef.PAPER.EXAM_ID.eq(examId)))
                        .one();
      
      if (paper == null)
        return Result.fail("未查询到相关试卷，可能是考试ID与试卷ID不匹配");
      
      //设置统一规范的文件名：试卷号-考试号-学生号.jpg
      String newFileName = paperId + "-" + paper.getExamId() + "-" + paper.getStudentId() + ".jpg";
      //转存到服本地务器
      file.transferTo(new File(imgUrl + newFileName));
      //更新数据库
      paper.setUrl("localhost:8080/fwwb202403/" + newFileName);
      paper.setHasUrl(1);
      paperMapper.update(paper);
      
      return Result.success("上传成功", "localhost:8080/fwwb202403/" + newFileName);
    } catch (Exception e) {
      e.printStackTrace();
    }
    return Result.error("上传异常");
  }
  
  public Result queryUnimportedBL(String examId) {
    System.out.println(123);
    System.out.println(examId);
    if(examId==null || examId.isEmpty()) return Result.fail("考试ID不能为空");
    
    List<Paper> papers = QueryChain.of(paperMapper)
                             .select(PaperTableDef.PAPER.DEFAULT_COLUMNS)
                             .from(PaperTableDef.PAPER)
                             .where(PaperTableDef.PAPER.EXAM_ID.eq(examId))
                             .and(PaperTableDef.PAPER.HAS_URL.eq(0))
                             .list();
    if(!papers.isEmpty()){
      return Result.success("查询成功",papers);
    }
    return Result.fail("查询失败");
  }
  
  public Result queryDS(String paperId) {
    
    PaperVo paperVo = QueryChain.of(Paper.class)
                      .select(PaperTableDef.PAPER.DEFAULT_COLUMNS,StudentTableDef.STUDENT.NAME.as("studentName"))
                      .from(PaperTableDef.PAPER,StudentTableDef.STUDENT)
                      .where(PaperTableDef.PAPER.ID.eq(paperId))
                      .and(PaperTableDef.PAPER.STUDENT_ID.eq(StudentTableDef.STUDENT.ID))
                      .oneAs(PaperVo.class);
    
    if(paperVo != null){
      return Result.success("查询成功",paperVo);
    }
    
    return Result.fail("查询失败(错误)");
  }
}
