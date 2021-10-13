package com.admin.quartz.service.impl;

import com.admin.quartz.entity.SysScheduleJobLogEntity;
import com.admin.quartz.repository.SysScheduleJobLogRepository;
import com.admin.quartz.service.SysScheduleJobLogService;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import com.admin.core.basic.AbstractServiceImpl;

/**
 * 定时任务日志(SysScheduleJobLog)表服务实现类
 *
 * @author fei
 * @since 2019-12-20 16:16:36
 */
@Slf4j
@Service
@Validated
public class SysScheduleJobLogServiceImpl extends AbstractServiceImpl<SysScheduleJobLogEntity, Long> implements SysScheduleJobLogService {
  private final SysScheduleJobLogRepository sysScheduleJobLogRepository;

  @Autowired
  public SysScheduleJobLogServiceImpl(SysScheduleJobLogRepository sysScheduleJobLogRepository) {
    super(sysScheduleJobLogRepository);
    this.sysScheduleJobLogRepository = sysScheduleJobLogRepository;
  }

}
