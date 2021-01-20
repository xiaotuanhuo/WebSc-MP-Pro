package com.sc.mp.quartz;

import org.quartz.JobDetail;
import org.quartz.Trigger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.quartz.MethodInvokingJobDetailFactoryBean;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;
import org.springframework.scheduling.quartz.SimpleTriggerFactoryBean;

@Configuration
public class QuartzConfigration {
	
	@Bean(name = "firstJobDetail")
	public MethodInvokingJobDetailFactoryBean firstJobDetail(ScheduleTask task) {
		 // ScheduleTask为需要执行的任务  
		 MethodInvokingJobDetailFactoryBean jobDetail = new MethodInvokingJobDetailFactoryBean();  
		 jobDetail.setConcurrent(false);  
		 jobDetail.setName("schedule_sendmsg");// 设置任务的名字  
		 jobDetail.setGroup("schedule_sendmsg_group");// 设置任务的分组，这些属性都可以存储在数据库中，在多任务的时候使用  
		
		 jobDetail.setTargetObject(task);  
		 jobDetail.setTargetMethod("schedule_sendmsg");
		 return jobDetail;  
	}
	
	@Bean(name = "firstTrigger")
    public SimpleTriggerFactoryBean firstTrigger(JobDetail firstJobDetail) {
		SimpleTriggerFactoryBean trigger = new SimpleTriggerFactoryBean();
		trigger.setJobDetail(firstJobDetail);
        // 设置任务启动延迟
        trigger.setStartDelay(0);
        // 每5秒执行一次
        trigger.setRepeatInterval(1000000);
        return trigger;
	}

    @Bean(name = "scheduler")  
    public SchedulerFactoryBean schedulerFactory(Trigger firstTrigger) {  
        SchedulerFactoryBean bean = new SchedulerFactoryBean();
        //设置是否任意一个已定义的Job会覆盖现在的Job。默认为false，即已定义的Job不会覆盖现有的Job。
        bean.setOverwriteExistingJobs(false);  
        // 延时启动，应用启动60秒后  ，定时器才开始启动
        bean.setStartupDelay(10);  
        // 注册定时触发器  
        bean.setTriggers(firstTrigger);  
        
//        bean.setTriggers();
        return bean;  
    }
    
    //多任务时的Scheduler，动态设置Trigger。一个SchedulerFactoryBean可能会有多个Trigger
    @Bean(name = "multitaskScheduler") 
    public SchedulerFactoryBean schedulerFactoryBean(){  
        SchedulerFactoryBean schedulerFactoryBean = new SchedulerFactoryBean();   
        return schedulerFactoryBean;   
    }
}