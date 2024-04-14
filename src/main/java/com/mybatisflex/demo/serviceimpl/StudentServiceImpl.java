package com.mybatisflex.demo.serviceimpl;

import com.mybatisflex.demo.entity.Student;
import com.mybatisflex.demo.entity.table.StudentTableDef;
import com.mybatisflex.core.query.QueryChain;
import com.mybatisflex.demo.mapper.StudentMapper;
import com.mybatisflex.demo.utils.Result;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Service
@Slf4j
public class StudentServiceImpl {
  
  @Autowired
  StudentMapper studentMapper;
  
  
  public Result addStudent(Student student) {
    
    int i = studentMapper.insertWithPk(student, true);
    if (i > 0) {
      return Result.success("新增学生成功", student.getId());
    }
    return Result.fail("新增学生失败");
  }
  
  public Result queryStudent(String id, String name, String classId, String grade) {
    System.out.println(id+" "+name+" "+classId+" "+grade);
    //四个条件独立查询，优先级向右递减
    
    if (!Objects.equals(id, "")) {
      System.out.println("id----");
      
      Student studentById = studentMapper.selectOneById(id);
      if (studentById == null)
        return Result.fail("查询学生失败");
      else {
        List<Student> list = new ArrayList<>();
        list.add(studentById);
        return Result.success("查询学生成功", list);
      }
    }
    
    if (!Objects.equals(name, "")) {
      System.out.println("name----");
      List<Student> studentByName = QueryChain.of(studentMapper)
                                        .select(StudentTableDef.STUDENT.DEFAULT_COLUMNS)
                                        .from(StudentTableDef.STUDENT)
                                        .where(StudentTableDef.STUDENT.NAME.like(name + "%"))
                                        .list();
      if (studentByName.isEmpty())
        return Result.fail("查询学生失败");
      else return Result.success("查询学生成功", studentByName);
    }
    
    if (!Objects.equals(classId, "")) {
      System.out.println("classid----");
      List<Student> studentByName = QueryChain.of(studentMapper)
                                        .select(StudentTableDef.STUDENT.DEFAULT_COLUMNS)
                                        .from(StudentTableDef.STUDENT)
                                        .where(StudentTableDef.STUDENT.CLASS_ID.eq(classId))
                                        .list();
      if (studentByName.isEmpty())
        return Result.fail("查询学生失败");
      else return Result.success("查询学生成功", studentByName);
    }
    
    if (grade != null) {
      System.out.println("grade----");
      List<Student> studentByName = QueryChain.of(studentMapper)
                                        .select(StudentTableDef.STUDENT.DEFAULT_COLUMNS)
                                        .from(StudentTableDef.STUDENT)
                                        .where(StudentTableDef.STUDENT.GRADE.eq(grade))
                                        .list();
      if (studentByName.isEmpty())
        return Result.fail("查询学生失败");
      else return Result.success("查询学生成功", studentByName);
    }
    
    return Result.error("查询学生错误，可能是没有合法条件");
  }
}

