package com.kenfei.admin.quartz.service.impl;

import com.kenfei.admin.core.common.exception.AppException;
import com.kenfei.admin.quartz.entity.SysScheduleJobEntity;
import com.kenfei.admin.quartz.repository.SysScheduleJobRepository;
import com.kenfei.admin.quartz.service.SysScheduleJobService;
import com.kenfei.admin.quartz.utils.CronUtils;
import com.kenfei.admin.quartz.utils.ScheduleConstants;
import com.kenfei.admin.quartz.utils.ScheduleUtils;
import com.kenfei.admin.quartz.utils.TaskException;
import org.quartz.JobDataMap;
import org.quartz.JobKey;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import com.kenfei.admin.core.common.base.AbstractServiceImpl;

import javax.annotation.PostConstruct;
import javax.validation.Valid;
import java.util.List;

/**
 * 定时任务(SysScheduleJob)表服务实现类
 *
 * @author fei
 * @since 2019-12-20 16:16:33
 */
@Slf4j
@Service
@Validated
public class SysScheduleJobServiceImpl extends AbstractServiceImpl<SysScheduleJobEntity, Long>
    implements SysScheduleJobService {
  private final SysScheduleJobRepository sysScheduleJobRepository;

  private final Scheduler scheduler;

  @Autowired
  public SysScheduleJobServiceImpl(
    SysScheduleJobRepository sysScheduleJobRepository, Scheduler scheduler) {
    super(sysScheduleJobRepository);
    this.sysScheduleJobRepository = sysScheduleJobRepository;
    this.scheduler = scheduler;
  }

  /** 项目启动时，初始化定时器 */
  @PostConstruct
  public void init() throws SchedulerException, TaskException {
    // 获取所有开启的任务
    scheduler.clear();
    // 获取所有开启的任务
    List<SysScheduleJobEntity> scheduleJobList = sysScheduleJobRepository.findAllByStatus(true);
    for (SysScheduleJobEntity job : scheduleJobList) {
      ScheduleUtils.createScheduleJob(scheduler, job);
    }
  }

  @Override
  public SysScheduleJobEntity update(@Valid SysScheduleJobEntity entity) {
    SysScheduleJobEntity properties = findById(entity.getId());
    entity = super.update(entity);

    try {
      updateSchedulerJob(entity, properties.getJobName());
    } catch (SchedulerException e) {
      throw new AppException(e.getMessage());
    }

    return entity;
  }

  @Override
  public SysScheduleJobEntity save(@Valid SysScheduleJobEntity entity) {
    try {
      // 添加任务
      ScheduleUtils.createScheduleJob(scheduler, entity);
    } catch (SchedulerException e) {
      throw new AppException(e.getMessage());
    }

    return super.save(entity);
  }

  @Override
  public void delete(SysScheduleJobEntity entity) {
    super.delete(entity);
  }

  @Override
  public SysScheduleJobEntity pauseJob(SysScheduleJobEntity job) throws SchedulerException {
    Long jobId = job.getId();
    String jobName = job.getJobName();
    job.setStatus(false);

    scheduler.pauseJob(ScheduleUtils.getJobKey(jobId, jobName));

    return update(job);
  }

  @Override
  public SysScheduleJobEntity resumeJob(SysScheduleJobEntity job) throws SchedulerException {
    Long jobId = job.getId();
    String jobName = job.getJobName();
    job.setStatus(true);

    scheduler.resumeJob(ScheduleUtils.getJobKey(jobId, jobName));

    return update(job);
  }

  @Override
  public void deleteJob(SysScheduleJobEntity job) throws SchedulerException {
    Long jobId = job.getId();
    String jobName = job.getJobName();

    scheduler.deleteJob(ScheduleUtils.getJobKey(jobId, jobName));

    delete(jobId);
  }

  @Override
  public void deleteJobByIds(Long... ids) throws SchedulerException {
    for (Long jobId : ids) {
      SysScheduleJobEntity job = findById(jobId);
      deleteJob(job);
    }
  }

  @Override
  public SysScheduleJobEntity changeStatus(SysScheduleJobEntity job) throws SchedulerException {
    Boolean status = job.getStatus();
    if (status) {
      resumeJob(job);
    } else {
      pauseJob(job);
    }
    return job;
  }

  @Override
  public void run(Long jobId) throws SchedulerException {
    SysScheduleJobEntity tmpObj = findById(jobId);
    // 参数
    JobDataMap dataMap = new JobDataMap();
    dataMap.put(ScheduleConstants.TASK_PROPERTIES, tmpObj);
    scheduler.triggerJob(ScheduleUtils.getJobKey(jobId, tmpObj.getJobName()), dataMap);
  }

  /**
   * 更新任务
   *
   * @param job 任务对象
   * @param jobName 任务名
   */
  public void updateSchedulerJob(SysScheduleJobEntity job, String jobName) throws SchedulerException, TaskException
  {
    Long jobId = job.getId();
    // 判断是否存在
    JobKey jobKey = ScheduleUtils.getJobKey(jobId, jobName);
    if (scheduler.checkExists(jobKey))
    {
      // 防止创建时存在数据问题 先移除，然后在执行创建操作
      scheduler.deleteJob(jobKey);
    }
    ScheduleUtils.createScheduleJob(scheduler, job);
  }

  @Override
  public boolean checkCronExpressionIsValid(String cronExpression) {
    return CronUtils.isValid(cronExpression);
  }
}
