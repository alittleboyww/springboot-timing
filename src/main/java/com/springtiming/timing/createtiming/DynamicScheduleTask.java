package com.springtiming.timing.createtiming;

import com.mysql.cj.util.StringUtils;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
@Configuration
@EnableScheduling
public class DynamicScheduleTask implements SchedulingConfigurer{
    @Mapper
    public interface CronMapper{
        @Select("select cron from cron limit 1")
        public String getCron();
    }

    @Autowired
    @SuppressWarnings("all")
    CronMapper cronMapper;

    @Override
    public void configureTasks(ScheduledTaskRegistrar scheduledTaskRegistrar) {
        scheduledTaskRegistrar.addTriggerTask(
                ()->System.out.println("执行动态定时任务： "+ LocalDateTime.now().toLocalTime()),
                triggerContext -> {
                    String cron = cronMapper.getCron();
                    if(StringUtils.isNullOrEmpty(cron)){

                    }
                    return new CronTrigger(cron).nextExecutionTime(triggerContext);
                }
        );
    }
}
