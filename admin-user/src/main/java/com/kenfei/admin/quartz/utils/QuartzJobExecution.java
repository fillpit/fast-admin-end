package com.kenfei.admin.quartz.utils;

import com.kenfei.admin.quartz.entity.SysScheduleJobEntity;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（允许并发执行）
 * @author fei
 * @since 2020/10/30 10:55 上午
 */
public class QuartzJobExecution extends AbstractQuartzJob {
  @Override
  protected void doExecute(JobExecutionContext context, SysScheduleJobEntity sysJob) throws Exception {
    JobInvokeUtil.invokeMethod(sysJob);
  }
}
