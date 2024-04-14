package com.mybatisflex.demo;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;


@SpringBootTest
class Proj240202ApplicationTests {

  
  @Test
  void contextLoads() {
   
    
    System.out.println("rows");
    
  
    
    
    
    /*//简单查询
    List<Account> accountList = accountMapper.selectAll();
    accountList.forEach(System.out::println);
    
    //有条件查询
    QueryWrapper queryWrapper = QueryWrapper.create();
    queryWrapper.select()
        .where(ACCOUNT.AGE.eq(18));
    Account account = accountMapper.selectOneByQuery(queryWrapper);
    System.out.println("123");
    System.out.println(account.getUserName());*/
  }
}
