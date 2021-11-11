package com.kenfei.admin.quartz.service;

import com.kenfei.admin.core.common.base.InterfaceService;
import com.kenfei.admin.quartz.entity.SysScheduleJobEntity;
import org.quartz.SchedulerException;

/**
 * 定时任务(SysScheduleJob)表服务接口
 *
 * @author fei
 * @since 2019-12-20 16:16:33
 */
public interface SysScheduleJobService extends InterfaceService<SysScheduleJobEntity, Long> {
  /**
   * 暂停任务
   *
   * @param job 调度信息
   * @return 结果
   */
  SysScheduleJobEntity pauseJob(SysScheduleJobEntity job) throws SchedulerException;

  /**
   * 恢复任务
   *
   * @param job 调度信息
   * @return 结果
   */
  SysScheduleJobEntity resumeJob(SysScheduleJobEntity job) throws SchedulerException;

  /**
   * 删除任务后，所对应的trigger也将被删除
   *
   * @param job 调度信息
   * @return 结果
   */
  void deleteJob(SysScheduleJobEntity job) throws SchedulerException;

  /**
   * 批量删除调度信息
   *
   * @param ids 需要删除的数据ID
   * @return 结果
   */
  void deleteJobByIds(Long... ids) throws SchedulerException;

  /**
   * 任务调度状态修改
   *
   * @param job 调度信息
   * @return 结果
   */
  SysScheduleJobEntity changeStatus(SysScheduleJobEntity job) throws SchedulerException;

  /**
   * 立即运行任务
   *
   * @param jobId 任务ID
   * @return 结果
   */
  void run(Long jobId) throws SchedulerException;

  /**
   * 校验cron表达式是否有效
   *
   * @param cronExpression 表达式
   * @return 结果
   */
  boolean checkCronExpressionIsValid(String cronExpression);
}
