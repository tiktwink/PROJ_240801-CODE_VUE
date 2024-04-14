package com.mybatisflex.demo.utils.jwt;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class MyInterceptorConfig implements WebMvcConfigurer { //拦截器的拦截和放行配置
  
  // 拦截器的加载在springcontext之前，所以自动注入的mapper是null
  // 为确保拦截器中的mapper正常运作，将拦截器进行注入
  @Bean
  public MyInterceptor myInterceptor(){ //与下方的注册函数一致
    return  new MyInterceptor();
  }
  
  
  @Override
  public void addInterceptors(InterceptorRegistry registry) {
    registry.addInterceptor(myInterceptor())  //与上方的Bean函数一致
        //放行的路径
        .excludePathPatterns("/user/login")
        .excludePathPatterns("/favicon.ico")
        .excludePathPatterns("/user/signin")
        .excludePathPatterns("/query/**") //“/**”表示通配符
        .excludePathPatterns("/tag/**")
        .excludePathPatterns("/upload")
        .excludePathPatterns("/fwwb202403/**")
        .excludePathPatterns("/fwwb202403checked/**")
    ;
    //对于不放行的路径，都在应用中的MyInterceptor实例中拦截处理
  }
}
