package com.springtiming.timing.createtiming;

import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;


//@Component注解用于对那些比较中立的类进行注释；
//相对与在持久层、业务层和控制层分别采用 @Repository、@Service 和 @Controller 对分层中的类进行注释
@Component
@Configuration      //1. 主要用于标记配置类，兼备Component的效果
@EnableScheduling   //2. 开启定时任务
public class SaticScheduleTask {
    //3. 添加定时任务
    //3.添加定时任务
    //@Scheduled(cron = "0/5 * * * * ?")
    @Scheduled(fixedRate = 5000)
    private void configureTasks(){
        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }
}
