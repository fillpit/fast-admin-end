package com.kenfei.admin.modules.quartz.dto;

import java.util.Date;

import com.kenfei.admin.modules.quartz.entity.SysScheduleJobLogEntity;
import com.kenfei.admin.core.common.base.DtoConvert;

import lombok.Data;

/**
 * 定时任务日志(SysScheduleJobLog)表单数据
 *
 * @author fei
 * @since 2019-12-20 16:16:36
 */
@Data
public class SysScheduleJobLogDto implements DtoConvert<SysScheduleJobLogEntity> {
  /** 任务日志id */
  private Long id;
  /** 任务id */
  private Long jobId;
  /** spring bean名称 */
  private String beanName;
  /** 方法名 */
  private String methodName;
  /** 参数 */
  private String params;
  /** 任务状态    0：失败    1：成功 */
  private Integer status;
  /** 失败信息 */
  private String error;
  /** 耗时(单位：毫秒) */
  private Integer times;
  /** 创建人 */
  private String createUser;
  /** 创建时间 */
  private Date createDate;
  /** 更新人 */
  private String updateUser;
  /** 更新时间 */
  private Date updateDate;

}
