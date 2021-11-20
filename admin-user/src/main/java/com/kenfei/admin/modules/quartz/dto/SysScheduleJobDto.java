package com.kenfei.admin.modules.quartz.dto;

import com.kenfei.admin.modules.quartz.entity.SysScheduleJobEntity;
import com.kenfei.admin.core.common.base.DtoConvert;

import com.kenfei.admin.modules.quartz.enums.ScheduleEnum;
import lombok.Data;


/**
 * 定时任务(SysScheduleJob)表单数据
 *
 * @author fei
 * @since 2019-12-20 16:16:36
 */
@Data
public class SysScheduleJobDto implements DtoConvert<SysScheduleJobEntity> {
  /** 任务名称 */
  private String jobName;
  /** spring bean名称 */
  private String beanName;
  /** 方法名 */
  private String methodName;
  /** 参数 */
  private String params;
  /** cron表达式 */
  private String cronExpression;
  /** 任务状态  0：暂停  1：正常 */
  private Boolean status;
  /** 并发执行(0=允许,1=禁止) */
  private Boolean concurrent;
  /** 备注 */
  private String remark;
  /** 负责人 */
  private String personInCharge;
  /** 报警邮箱 */
  private String email;

  @Override
  public SysScheduleJobEntity convert(Class<SysScheduleJobEntity> sysQuartzJobEntityClass) {
    SysScheduleJobEntity entity = DtoConvert.super.convert(sysQuartzJobEntityClass);
    entity.setMisfirePolicy(ScheduleEnum.MISFIRE_DEFAULT.value());

    return entity;
  }
}
