package com.mybatisflex.demo.serviceimpl;

import com.mybatisflex.demo.entity.Subject;
import com.mybatisflex.demo.mapper.SubjectMapper;
import com.mybatisflex.demo.entity.table.SubjectTableDef;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public class SubjectServiceImpl {
  @Autowired
  SubjectMapper subjectMapper;
  
  
  public Result queryAll() {
    List<Subject> list = QueryChain.of(Subject.class)
                             .select(SubjectTableDef.SUBJECT.DEFAULT_COLUMNS)
                             .from(SubjectTableDef.SUBJECT)
                             .list();
    if (!list.isEmpty()) {
      return Result.success("查询科目成功", list);
    } else {
      return Result.fail("查询科目失败");
    }
    
  }
  
  public Result add(Subject subject) {
    
    int i = subjectMapper.insertWithPk(subject,true);
    if (i > 0) {
      return Result.success("新增科目成功");
    } else {
      return Result.fail("新增科目失败");
    }
  }
  
  public Result delete(String id) {
    int i  = subjectMapper.deleteById(id);
    if(i>0){
      return Result.success("删除科目成功",id);
    }
    
    return Result.fail("删除科目失败");
    
  }
}
