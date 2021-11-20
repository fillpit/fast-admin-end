package com.kenfei.admin.modules.quartz.repository;

import com.kenfei.admin.modules.quartz.entity.SysScheduleJobEntity;
import com.kenfei.admin.core.common.base.BaseRepository;

import java.util.List;

/**
 * 定时任务(SysScheduleJob)表数据库访问层
 *
 * @author fei
 * @since 2019-12-20 16:16:35
 */
public interface SysScheduleJobRepository extends BaseRepository<SysScheduleJobEntity, Long> {

  /**
   * 根据状态获取定时任务集合
   * @param status 状态
   * @return 定时任务集合
   */
  List<SysScheduleJobEntity> findAllByStatus(Boolean status);
}
