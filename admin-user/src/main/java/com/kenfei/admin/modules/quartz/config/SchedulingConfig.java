package com.kenfei.admin.modules.quartz.config;

import org.quartz.Scheduler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.TaskScheduler;
import org.springframework.scheduling.concurrent.ThreadPoolTaskScheduler;
import org.springframework.scheduling.quartz.SchedulerFactoryBean;

import java.util.Properties;

/**
 * 定时任务 配置类
 *
 * @author fei
 * @since 2019/12/20 16:18
 */
@Configuration
public class SchedulingConfig {

  @Bean
  public TaskScheduler taskScheduler() {
    ThreadPoolTaskScheduler taskScheduler = new ThreadPoolTaskScheduler();
    // 定时任务执行线程池核心线程数
    taskScheduler.setPoolSize(4);
    taskScheduler.setRemoveOnCancelPolicy(true);
    taskScheduler.setThreadNamePrefix("TaskSchedulerThreadPool-");
    return taskScheduler;
  }

  @Bean
  public SchedulerFactoryBean schedulerFactoryBean() {
    SchedulerFactoryBean factory = new SchedulerFactoryBean();
//    factory.setDataSource(dataSource);
//    DataSource dataSource
    // quartz参数
    Properties prop = new Properties();
//    prop.put("org.quartz.scheduler.instanceName", "RuoyiScheduler");
//    prop.put("org.quartz.scheduler.instanceId", "AUTO");
    // 线程池配置
    prop.put("org.quartz.threadPool.class", "org.quartz.simpl.SimpleThreadPool");
    prop.put("org.quartz.threadPool.threadCount", "20");
    prop.put("org.quartz.threadPool.threadPriority", "5");
    // JobStore配置
//    prop.put("org.quartz.jobStore.class", "org.quartz.impl.jdbcjobstore.JobStoreTX");
    // 集群配置
//    prop.put("org.quartz.jobStore.isClustered", "true");
//    prop.put("org.quartz.jobStore.clusterCheckinInterval", "15000");
//    prop.put("org.quartz.jobStore.maxMisfiresToHandleAtATime", "1");
//    prop.put("org.quartz.jobStore.txIsolationLevelSerializable", "true");

    // sqlserver 启用
    // prop.put("org.quartz.jobStore.selectWithLockSQL", "SELECT * FROM {0}LOCKS UPDLOCK WHERE
    // LOCK_NAME = ?");
//    prop.put("org.quartz.jobStore.misfireThreshold", "12000");
//    prop.put("org.quartz.jobStore.tablePrefix", "QRTZ_");
    factory.setQuartzProperties(prop);

    factory.setSchedulerName("RuoyiScheduler");
    // 延时启动
    factory.setStartupDelay(1);
    factory.setApplicationContextSchedulerContextKey("applicationContextKey");
    // 可选，QuartzScheduler
    // 启动时更新己存在的Job，这样就不用每次修改targetObject后删除qrtz_job_details表对应记录了
//    factory.setOverwriteExistingJobs(true);
    // 设置自动启动，默认为true
    factory.setAutoStartup(true);

    return factory;
  }

  @Bean
  public Scheduler scheduler() {
    return schedulerFactoryBean().getScheduler();
  }
}
