package com.mybatisflex.demo.serviceimpl;

import com.mybatisflex.demo.entity.Exam;
import com.mybatisflex.demo.entity.table.ExamTableDef;
import com.mybatisflex.demo.entity.table.PaperTableDef;
import com.mybatisflex.demo.entity.table.SubjectTableDef;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.core.query.QueryMethods;
import com.mybatisflex.demo.entity.vo.ExamVo;
import com.mybatisflex.demo.mapper.ExamMapper;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

import static com.mybatisflex.core.query.QueryMethods.*;

@Service
@Slf4j
public class ExamServiceImpl {
  
  @Autowired
  ExamMapper examMapper;
  
  public Result addExam(Exam exam) {
    
    int i = examMapper.insertWithPk(exam, true); //ignore null value to insert
    if (i > 0) {
      return Result.success("新增考试成功", exam.getId());
    }
    
    return Result.fail("新增考试失败(错误)");
  }
  
  public Result queryOne(String id) {
//    System.out.println(id);
//    Exam exam = examMapper.selectOneById(id);
    ExamVo examVo = QueryChain.of(examMapper)
                        .select(ExamTableDef.EXAM.DEFAULT_COLUMNS,
                            QueryMethods.count(PaperTableDef.PAPER.ID).as("num"),
                            count(case_().when(PaperTableDef.PAPER.HAS_URL.eq(1)).then(1).end()).as("countHasUrl"),
                            count(case_().when(PaperTableDef.PAPER.TESTED.eq(0)).then(1).end()).as("countUntested"),
                            count(case_().when(PaperTableDef.PAPER.CHECKED.eq(0)).then(1).end()).as("countUnchecked")
                        )
                        .from(ExamTableDef.EXAM)
                        .leftJoin(PaperTableDef.PAPER).on(ExamTableDef.EXAM.ID.eq(PaperTableDef.PAPER.EXAM_ID))
                        .groupBy(ExamTableDef.EXAM.ID)
                        .where(ExamTableDef.EXAM.ID.eq(id))
                        .oneAs(ExamVo.class);
    
    if (examVo != null) {
      return Result.success("查询单个考试成功", examVo);
    }
    return Result.fail("查询单个考试失败(错误)");
  }
  
  public Result queryMul(String name, String subjectId, String subjectTitle, Integer grade) {

//                                 ,
//                                 QueryMethods.count(PAPER.TESTED.eq(0).getColumn()).as("countUntested"),
//                                 QueryMethods.count(PAPER.HAS_URL.eq(1).getColumn()).as("hasUrl"),
//                                 QueryMethods.count(PAPER.CHECKED.eq(0).getColumn()).as("countUnchecked")
    
    QueryChain<Exam> q = QueryChain.of(examMapper)
                             .select(ExamTableDef.EXAM.DEFAULT_COLUMNS,
                                 QueryMethods.count(PaperTableDef.PAPER.ID).as("num"),
                                 count(case_().when(PaperTableDef.PAPER.HAS_URL.eq(1)).then(1).end()).as("countHasUrl"),
                                 count(case_().when(PaperTableDef.PAPER.TESTED.eq(0)).then(1).end()).as("countUntested"),
                                 count(case_().when(PaperTableDef.PAPER.CHECKED.eq(0)).then(1).end()).as("countUnchecked")
                             )
                             .from(ExamTableDef.EXAM)
                             .leftJoin(PaperTableDef.PAPER).on(ExamTableDef.EXAM.ID.eq(PaperTableDef.PAPER.EXAM_ID))
                             .leftJoin(SubjectTableDef.SUBJECT).on(ExamTableDef.EXAM.SUBJECT_ID.eq(SubjectTableDef.SUBJECT.ID))
                             .groupBy(ExamTableDef.EXAM.ID);
    
    if (!Objects.equals(name, "") && name != null)
      q.and(ExamTableDef.EXAM.NAME.like("%" + name + "%"));
    if (!Objects.equals(subjectId, "") && subjectId != null)
      q.and(SubjectTableDef.SUBJECT.ID.eq(subjectId));
    if (!Objects.equals(subjectTitle, "") && subjectTitle != null)
      q.and(SubjectTableDef.SUBJECT.TITLE.like("%" + subjectTitle + "%"));
    if (grade != null)
      q.and(SubjectTableDef.SUBJECT.SUGS_GRADE.eq(grade));
    
    List<ExamVo> examVoList = q.listAs(ExamVo.class);
    
    return Result.success("查询成功", examVoList);
  }
  
  public Result queryMul1(String name, String subjectId, String subjectTitle, Integer grade) {
    
    QueryChain<Exam> q = QueryChain.of(examMapper)
                             .select(ExamTableDef.EXAM.DEFAULT_COLUMNS,
                                 QueryMethods.count(PaperTableDef.PAPER.ID).as("num"))
                             .from(ExamTableDef.EXAM)
                             .leftJoin(PaperTableDef.PAPER).on(ExamTableDef.EXAM.ID.eq(PaperTableDef.PAPER.EXAM_ID))
                             .leftJoin(SubjectTableDef.SUBJECT).on(ExamTableDef.EXAM.SUBJECT_ID.eq(SubjectTableDef.SUBJECT.ID))
                             .groupBy(ExamTableDef.EXAM.ID);
    
    if (!Objects.equals(name, "") && name != null)
      q.and(ExamTableDef.EXAM.NAME.like("%" + name + "%"));
    if (!Objects.equals(subjectId, "") && subjectId != null)
      q.and(SubjectTableDef.SUBJECT.ID.eq(subjectId));
    if (!Objects.equals(subjectTitle, "") && subjectTitle != null)
      q.and(SubjectTableDef.SUBJECT.TITLE.like("%" + subjectTitle + "%"));
    if (grade != null)
      q.and(SubjectTableDef.SUBJECT.SUGS_GRADE.eq(grade));
    
    List<ExamVo> examVoList = q.listAs(ExamVo.class);
    
    return Result.success("查询成功", examVoList);
    
  }
  
  public Result queryCount(String mgrId) {
    
    Long i = QueryChain.of(examMapper)
                 .select(ExamTableDef.EXAM.ID)
                 .from(ExamTableDef.EXAM)
                 .count();
    
    return Result.success("查询成功", i);
  }
}
