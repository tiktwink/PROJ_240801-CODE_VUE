package com.mybatisflex.demo.controller;


import com.mybatisflex.demo.entity.table.ExamTableDef;
import com.mybatisflex.demo.entity.table.PaperTableDef;
import com.mybatisflex.demo.serviceimpl.ExamServiceImpl;
import com.mybatisflex.demo.utils.Result;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.demo.entity.Exam;
import com.mybatisflex.demo.entity.vo.ExamVo;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static com.mybatisflex.core.query.QueryMethods.case_;
import static com.mybatisflex.core.query.QueryMethods.count;

@Slf4j
@RestController
@RequestMapping("/exam")
public class ExamController {
  
  @Autowired
  ExamServiceImpl examService;
  
  @PostMapping("/index")
  public Result addExam(@RequestBody Exam exam) {
    //忽略鉴权
    
    return examService.addExam(exam);
  }
  
  @GetMapping("/index")
  public Result queryOne(String id) {
    //忽略鉴权
    
    return examService.queryOne(id);
  }
  
  @GetMapping("/indexs")
  public Result queryMul(String id, String name, String subjectId, String subjectTitle, Integer grade) {
    //忽略鉴权
    
    if (!Objects.equals(id, "") && id != null) {
      
      ExamVo examVo = QueryChain.of(Exam.class)
                          .select(ExamTableDef.EXAM.DEFAULT_COLUMNS,
                              QueryMethods.count(PaperTableDef.PAPER.ID).as("num"),
                              count(case_().when(PaperTableDef.PAPER.HAS_URL.eq(1)).then(1).end()).as("countHasUrl"),
                              count(case_().when(PaperTableDef.PAPER.TESTED.eq(0)).then(1).end()).as("countUntested"),
                              count(case_().when(PaperTableDef.PAPER.CHECKED.eq(0)).then(1).end()).as("countUnchecked"))
                          .from(ExamTableDef.EXAM, PaperTableDef.PAPER)
                          .where(ExamTableDef.EXAM.ID.eq(id))
                          .and(ExamTableDef.EXAM.ID.eq(PaperTableDef.PAPER.EXAM_ID))
                          .groupBy(ExamTableDef.EXAM.ID)
                          .oneAs(ExamVo.class);
      
      if (examVo != null) {
        List<ExamVo> list = new ArrayList<>();
        list.add(examVo);
        return Result.success("查询成功", list);
      }
      
      return Result.fail("查询失败");
    }
    
    return examService.queryMul(name, subjectId, subjectTitle, grade);
  }
  
  @GetMapping("/count")
  public Result queryCount(String mgrId) {
    if (Objects.equals(mgrId, "")) mgrId = null;
    
    return examService.queryCount(mgrId);
  }
}


