package com.kenfei.admin.quartz.utils;

import com.kenfei.admin.core.utils.ExceptionUtil;
import com.kenfei.admin.core.utils.SpringContextUtils;
import com.kenfei.admin.quartz.entity.SysScheduleJobEntity;
import com.kenfei.admin.quartz.entity.SysScheduleJobLogEntity;
import com.kenfei.admin.quartz.service.SysScheduleJobLogService;
import com.kenfei.admin.utils.EmailUtils;
import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;

import java.util.Date;

/**
 * 任务调度通用常量
 * @author fei
 * @since 2020/10/27 10:57 上午
 */
public abstract class AbstractQuartzJob implements Job {
  private static final Logger log = LoggerFactory.getLogger(AbstractQuartzJob.class);

  /** 线程本地变量 */
  private static ThreadLocal<Date> THREAD_LOCAL = new ThreadLocal<>();

  @Override
  public void execute(JobExecutionContext context) throws JobExecutionException {
    SysScheduleJobEntity sysJob = new SysScheduleJobEntity();
    BeanUtils.copyProperties(sysJob, context.getMergedJobDataMap().get("TASK_PROPERTIES"));
    try {
      before(context, sysJob);
      doExecute(context, sysJob);
      after(context, sysJob, null);
    } catch (Exception e) {
      log.error("任务执行异常  - ：", e);
      after(context, sysJob, e);
    }
  }

  /**
   * 执行前
   *
   * @param context 工作执行上下文对象
   * @param sysJob 系统计划任务
   */
  protected void before(JobExecutionContext context, SysScheduleJobEntity sysJob) {
    THREAD_LOCAL.set(new Date());
  }

  /**
   * 执行后
   *
   * @param context 工作执行上下文对象
   * @param sysJob 系统计划任务
   */
  protected void after(JobExecutionContext context, SysScheduleJobEntity sysJob, Exception e) {
    Date startTime = THREAD_LOCAL.get();
    THREAD_LOCAL.remove();

    final SysScheduleJobLogEntity sysJobLog = new SysScheduleJobLogEntity();
    sysJobLog.setJobName(sysJob.getJobName());
    String invokeTarget = StringUtils.format("%s.%s(%s)", sysJob.getBeanName(), sysJob.getMethodName(), sysJob.getParams());
    sysJobLog.setInvokeTarget(invokeTarget);
    sysJobLog.setStartTime(startTime);
    sysJobLog.setEndTime(new Date());
    long runMs = sysJobLog.getEndTime().getTime() - sysJobLog.getStartTime().getTime();
    sysJobLog.setJobMessage(sysJobLog.getJobName() + " 总共耗时：" + runMs + "毫秒");
    if (e != null) {
      sysJobLog.setStatus(false);
      String errorMsg = ExceptionUtil.getStackTrace(e, 1);
      sysJobLog.setExceptionInfo(errorMsg);
    } else {
      sysJobLog.setStatus(true);
    }
    // 写入数据库当中
    SpringContextUtils.getBean(SysScheduleJobLogService.class).save(sysJobLog);

    // 发送通知邮件
    String email = sysJob.getEmail();
    if (StringUtils.hasLength(email)) {
      EmailUtils emailUtils = SpringContextUtils.getBean(EmailUtils.class);
      emailUtils.sendMimeMessge(email, "定时任务执行失败通知", ExceptionUtil.getStackTrace(e, 20));
    }
  }

  /**
   * 执行方法，由子类重载
   *
   * @param context 工作执行上下文对象
   * @param sysJob 系统计划任务
   * @throws Exception 执行过程中的异常
   */
  protected abstract void doExecute(JobExecutionContext context, SysScheduleJobEntity sysJob)
      throws Exception;
}
