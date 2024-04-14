package com.mybatisflex.demo.controller;

import com.mybatisflex.demo.entity.Exam;
import com.mybatisflex.demo.entity.Paper;
import com.mybatisflex.demo.entity.Student;
import com.mybatisflex.demo.entity.vo.ExamVo;
import com.mybatisflex.demo.utils.CallPythonScript;
import com.mybatisflex.demo.entity.table.ExamTableDef;
import com.mybatisflex.demo.entity.table.PaperTableDef;
import com.mybatisflex.demo.entity.table.StudentTableDef;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.core.update.UpdateChain;
import com.mybatisflex.demo.utils.Result;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.Date;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.atomic.AtomicInteger;

import static com.mybatisflex.core.query.QueryMethods.case_;
import static com.mybatisflex.core.query.QueryMethods.count;

@RestController
@RequestMapping("/check")
public class CheckController {
  
  
  @PostMapping("/paper-single/{id}")
  public Result paperSingleCheck(@PathVariable String id) {
//    Paper p = QueryChain.of(Paper.class)
//                       .select(PAPER.STUDENT_ID,PAPER.EXAM_ID)
//                       .from(PAPER)
//                       .where(PAPER.ID.eq(id))
//                       .one();
    
    Exam exam = QueryChain.of(Exam.class)
                    .select(ExamTableDef.EXAM.DEFAULT_COLUMNS)
                    .from(ExamTableDef.EXAM, PaperTableDef.PAPER)
                    .where(ExamTableDef.EXAM.ID.eq(PaperTableDef.PAPER.EXAM_ID))
                    .and(PaperTableDef.PAPER.ID.eq(id))
                    .one();
    String examId = exam.getId();
    
    Student student = QueryChain.of(Student.class)
                          .select(StudentTableDef.STUDENT.DEFAULT_COLUMNS)
                          .from(StudentTableDef.STUDENT, PaperTableDef.PAPER)
                          .where(StudentTableDef.STUDENT.ID.eq(PaperTableDef.PAPER.STUDENT_ID))
                          .and(PaperTableDef.PAPER.ID.eq(id))
                          .one();
    String studentId = student.getId();
    
    String paperPath = "D:/Warehouse/file/fwwb202403/" + id + "-" + examId + "-" + studentId + ".jpg";
    
    String paperCheckedPath = "D:/Warehouse/file/fwwb202403checked/" + id + "-" + examId + "-" + studentId + ".json";
    
    Boolean b = CallPythonScript.singleCheckOnPaper(paperPath, paperCheckedPath);
    
    if (b) {
      String jsonStr;
      Integer judge;
      Integer baseScore = 100;
      Integer judgeScore;
      
      try {
        File jsonFile = new File(paperCheckedPath);
        Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
        int ch = 0;
        StringBuffer stringBuffer = new StringBuffer();
        while ((ch = reader.read()) != -1) {
          stringBuffer.append((char) ch);
        }
        reader.close();
        jsonStr = stringBuffer.toString();
        System.out.println("以下是读取的json文件：");
        System.out.println(jsonStr);
        
        JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
        if (jsonObject.get("key").getAsJsonObject().get("judge").getAsString() == "0") {
          judge = 0;
          judgeScore = baseScore * 1;
          System.out.println("judgeScore = " + judgeScore);
        } else {
          judge = 1;
          judgeScore = 0;
        }
        
        boolean bool = UpdateChain.of(Paper.class)
                           .set(PaperTableDef.PAPER.CHECKED, 1)
                           .set(PaperTableDef.PAPER.CHECK_TIME, new Date())
                           .set(PaperTableDef.PAPER.JUDGE, judge)
                           .set(PaperTableDef.PAPER.JUDGE_SCORE, judgeScore)
                           .where(PaperTableDef.PAPER.ID.eq(id))
                           .update();
        if (bool) {
          System.out.println("数据库修改成功");
          
          ExamVo examVo = QueryChain.of(Exam.class)
                              .select(ExamTableDef.EXAM.DEFAULT_COLUMNS,
                                  QueryMethods.count(PaperTableDef.PAPER.ID).as("num"),
                                  count(case_().when(PaperTableDef.PAPER.CHECKED.eq(0)).then(1).end()).as("countUnchecked"))
                              .from(ExamTableDef.EXAM, PaperTableDef.PAPER)
                              .where(ExamTableDef.EXAM.ID.eq(PaperTableDef.PAPER.EXAM_ID))
                              .and(ExamTableDef.EXAM.ID.eq(examId))
                              .groupBy(ExamTableDef.EXAM.ID)
                              .oneAs(ExamVo.class);

//          System.out.println("以下是查询到的examvo：");
//          System.out.println(examVo.toString());
          
          if (examVo.getCountUnchecked() == 0 && examVo.getNum() > 0) {
            UpdateChain.of(Exam.class)
                .set(ExamTableDef.EXAM.CHECKED, 1)
                .set(ExamTableDef.EXAM.CHECK_TIME, new Date())
                .where(ExamTableDef.EXAM.ID.eq(examId))
                .update();
          }
          
          
        }
        
      } catch (Exception e) {
        e.printStackTrace();
        return Result.fail("文件读写异常");
      }
      
      return Result.success("答卷(" + id + ")评阅成功", id);
      
    } else {
      return Result.fail("答卷(" + id + ")评阅失败", id);
    }
  }
  
  
  @PostMapping("/exam-single/{id}")
  public Result examSingleCheck(@PathVariable String id) {
    List<Paper> paperList = QueryChain.of(Paper.class)
                                .select(PaperTableDef.PAPER.DEFAULT_COLUMNS)
                                .from(PaperTableDef.PAPER)
                                .where(PaperTableDef.PAPER.EXAM_ID.eq(id))
                                .list();
    
    if (!paperList.isEmpty()) {
      //设置计数变量
      AtomicInteger count = new AtomicInteger();
      
      paperList.forEach(item -> {
        String paperId = item.getId();
        String studentId = item.getStudentId();
        String paperPath = "D:/Warehouse/file/fwwb202403/" + paperId + "-" + id + "-" + studentId + ".jpg";
        String paperCheckedPath = "D:/Warehouse/file/fwwb202403checked/" + paperId + "-" + id + "-" + studentId + ".json";
        
        Boolean b = CallPythonScript.singleCheckOnPaper(paperPath, paperCheckedPath);
        
        if (b) {
          count.getAndIncrement();
          String jsonStr;
          Integer judge;
          Integer baseScore = 100;
          Integer judgeScore;
          
          
          
          try {
            File jsonFile = new File(paperCheckedPath);
            Reader reader = new InputStreamReader(new FileInputStream(jsonFile), "utf-8");
            int ch = 0;
            StringBuffer stringBuffer = new StringBuffer();
            while ((ch = reader.read()) != -1) {
              stringBuffer.append((char) ch);
            }
            reader.close();
            jsonStr = stringBuffer.toString();
            System.out.println("以下是读取的json文件：");
            System.out.println(jsonStr);
            
            JsonObject jsonObject = JsonParser.parseString(jsonStr).getAsJsonObject();
            if (Objects.equals(jsonObject.get("key").getAsJsonObject().get("judge").getAsString(), "0")) {
              judge = 0;
              judgeScore = baseScore * 1;
              System.out.println("judgeScore = " + judgeScore);
            } else {
              judge = 1;
              judgeScore = 0;
            }
            
            UpdateChain.of(Paper.class)
                .set(PaperTableDef.PAPER.CHECKED, 1)
                .set(PaperTableDef.PAPER.CHECK_TIME, new Date())
                .set(PaperTableDef.PAPER.JUDGE, judge)
                .set(PaperTableDef.PAPER.JUDGE_SCORE, judgeScore)
                .where(PaperTableDef.PAPER.ID.eq(paperId))
                .update();
            
          } catch (Exception e) {
            e.printStackTrace();
          }
          
        }
      });
      
      if (count.get() == paperList.size()) {
        Paper paper = QueryChain.of(Paper.class)
                          .select(PaperTableDef.PAPER.DEFAULT_COLUMNS)
                          .from(PaperTableDef.PAPER)
                          .where(PaperTableDef.PAPER.EXAM_ID.eq(id))
                          .one();
        
        ExamVo examVo = QueryChain.of(Exam.class)
                            .select(ExamTableDef.EXAM.DEFAULT_COLUMNS,
                                QueryMethods.count(PaperTableDef.PAPER.ID).as("num"))
                            .from(ExamTableDef.EXAM, PaperTableDef.PAPER)
                            .where(ExamTableDef.EXAM.ID.eq(paper.getExamId()))
                            .and(ExamTableDef.EXAM.ID.eq(PaperTableDef.PAPER.EXAM_ID))
                            .oneAs(ExamVo.class);
        
        if (examVo.getNum() == count.get() && count.get() != 0) {
          //将考试设为已阅
          boolean bool = UpdateChain.of(Exam.class)
                             .set(ExamTableDef.EXAM.CHECKED, 1)
                             .set(ExamTableDef.EXAM.CHECK_TIME, new Date())
                             .where(ExamTableDef.EXAM.ID.eq(examVo.getId()))
                             .update();
        }
        
        return Result.success("评阅完成");
      }
    }
    
    return Result.fail("评阅失败");
  }
}
