package com.kenfei.admin.modules.quartz.utils;

import com.kenfei.admin.modules.quartz.entity.SysScheduleJobEntity;
import com.kenfei.admin.modules.quartz.enums.ScheduleEnum;
import org.quartz.*;

/**
 * 定时任务工具类
 *
 * @author fei
 * @since 2020/10/28 4:50 下午
 */
public class ScheduleUtils {
  /**
   * 得到quartz任务类
   *
   * @param sysJob 执行计划
   * @return 具体执行任务类
   */
  private static Class<? extends Job> getQuartzJobClass(SysScheduleJobEntity sysJob) {
    boolean isConcurrent = sysJob.getConcurrent();
    return isConcurrent ? QuartzJobExecution.class : QuartzDisallowConcurrentExecution.class;
  }

  /** 构建任务触发对象 */
  public static TriggerKey getTriggerKey(Long jobId, String jobGroup) {
    return TriggerKey.triggerKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobGroup);
  }

  /** 构建任务键对象 */
  public static JobKey getJobKey(Long jobId, String jobName) {
    return JobKey.jobKey(ScheduleConstants.TASK_CLASS_NAME + jobId, jobName);
  }

  /** 创建定时任务 */
  public static void createScheduleJob(Scheduler scheduler, SysScheduleJobEntity job)
      throws SchedulerException, TaskException {
    Class<? extends Job> jobClass = getQuartzJobClass(job);
    // 构建job信息
    Long jobId = job.getId();
    String jobName = job.getJobName();
    JobDetail jobDetail =
        JobBuilder.newJob(jobClass).withIdentity(getJobKey(jobId, jobName)).build();

    // 表达式调度构建器
    CronScheduleBuilder cronScheduleBuilder =
        CronScheduleBuilder.cronSchedule(job.getCronExpression());
    cronScheduleBuilder = handleCronScheduleMisfirePolicy(job, cronScheduleBuilder);

    // 按新的cronExpression表达式构建一个新的trigger
    CronTrigger trigger =
        TriggerBuilder.newTrigger()
            .withIdentity(getTriggerKey(jobId, jobName))
            .withSchedule(cronScheduleBuilder)
            .build();

    // 放入参数，运行时的方法可以获取
    jobDetail.getJobDataMap().put(ScheduleConstants.TASK_PROPERTIES, job);

    // 判断是否存在
    if (scheduler.checkExists(getJobKey(jobId, jobName))) {
      // 防止创建时存在数据问题 先移除，然后在执行创建操作
      scheduler.deleteJob(getJobKey(jobId, jobName));
    }

    scheduler.scheduleJob(jobDetail, trigger);

    // 暂停任务
    if (job.getStatus().equals(false)) {
      scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobName));
    }
  }

  /** 设置定时任务策略 */
  public static CronScheduleBuilder handleCronScheduleMisfirePolicy(
    SysScheduleJobEntity job, CronScheduleBuilder cb) throws TaskException {
    ScheduleEnum scheduleEnum = ScheduleEnum.get(job.getMisfirePolicy());
    switch (scheduleEnum) {
      case MISFIRE_DEFAULT:
        return cb;
      case MISFIRE_IGNORE_MISFIRES:
        return cb.withMisfireHandlingInstructionIgnoreMisfires();
      case MISFIRE_FIRE_AND_PROCEED:
        return cb.withMisfireHandlingInstructionFireAndProceed();
      case MISFIRE_DO_NOTHING:
        return cb.withMisfireHandlingInstructionDoNothing();
      default:
        throw new TaskException(
            "The task misfire policy '"
                + job.getMisfirePolicy()
                + "' cannot be used in cron schedule tasks");
    }
  }
}
