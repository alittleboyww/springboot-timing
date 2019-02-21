1. 添加静态定时任务
    
    -   新建一个springboot项目
    -   创建一个类，代码如下
    ```
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

    ```
    
2. 添加动态定时任务

    - 添加依赖,在pom.xml中添加如下依赖
    ```
        <dependency>
			<groupId>mysql</groupId>
			<artifactId>mysql-connector-java</artifactId>
		</dependency>

		<dependency>
			<groupId>org.mybatis.spring.boot</groupId>
			<artifactId>mybatis-spring-boot-starter</artifactId>
			<version>1.3.1</version>
		</dependency>

		<dependency>
			<groupId>org.mybatis</groupId>
			<artifactId>mybatis</artifactId>
			<version>3.4.5</version>
		</dependency>
    ```
    - 创建数据库表并添加值，如下
    ```
    DROP DATABASE IF EXISTS `socks`;
    CREATE DATABASE `socks`;
    USE `SOCKS`;
    DROP TABLE IF EXISTS `cron`;
    CREATE TABLE `cron`  (
    `cron_id` VARCHAR(30) NOT NULL PRIMARY KEY,
    `cron` VARCHAR(30) NOT NULL  
    );
    INSERT INTO `cron` VALUES ('1', '0/5 * * * * ?');
    ```
    - 配置application.a...,如下
    ```
    spring.datasource.url=jdbc:mysql://localhost:3306/socks
    spring.datasource.username=root
    spring.datasource.password=l573324982
    ```
    - 创建定时器类，如下
    ```
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

    ```
    
3. 创建多线程定时任务

    - 创建类，代码如下
    ```
    import org.springframework.scheduling.annotation.Async;
    import org.springframework.scheduling.annotation.EnableAsync;
    import org.springframework.scheduling.annotation.EnableScheduling;
    import org.springframework.scheduling.annotation.Scheduled;
    import org.springframework.stereotype.Component;

    import java.time.LocalDateTime;

    @Component
    @EnableScheduling   //开启定时任务
    @EnableAsync        //开启多线程
    public class MultithreadScheduleTask {
        @Async
        @Scheduled(fixedDelay = 1000)
        public void first() throws InterruptedException{
            System.out.println("第一个定时任务开始：" + LocalDateTime.now().toLocalTime() + "\r\n线程: " + Thread.currentThread().getName());
            System.out.println();
            Thread.sleep(1000 * 10);
        }
        @Async
        @Scheduled(fixedDelay = 2000)
        public void second() {
            System.out.println("第二个定时任务开始 : " + LocalDateTime.now().toLocalTime() + "\r\n线程 : " + Thread.currentThread().getName());
            System.out.println();
        }
    }

    ```
    
4. 以上代码经过测试有效具体可[查看](https://www.cnblogs.com/mmzs/p/10161936.html)