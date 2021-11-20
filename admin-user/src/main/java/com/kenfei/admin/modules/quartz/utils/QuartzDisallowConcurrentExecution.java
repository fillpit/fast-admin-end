package com.kenfei.admin.modules.quartz.utils;

import com.kenfei.admin.modules.quartz.entity.SysScheduleJobEntity;
import org.quartz.DisallowConcurrentExecution;
import org.quartz.JobExecutionContext;

/**
 * 定时任务处理（禁止并发执行）
 *
 * @author fei
 * @since 2020/10/30 10:54 上午
 */
@DisallowConcurrentExecution
public class QuartzDisallowConcurrentExecution extends AbstractQuartzJob {
  @Override
  protected void doExecute(JobExecutionContext context, SysScheduleJobEntity sysJob) throws Exception {
    JobInvokeUtil.invokeMethod(sysJob);
  }
}
