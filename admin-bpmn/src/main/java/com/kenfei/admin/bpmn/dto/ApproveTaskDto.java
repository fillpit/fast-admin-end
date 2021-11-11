package com.kenfei.admin.bpmn.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.bpmn.entity.ApproveTaskEntity;
import lombok.Data;
import org.flowable.engine.impl.bpmn.parser.handler.UserTaskParseHandler;

import javax.validation.constraints.NotBlank;

/**
 * 激活的任务(ApproveTask)表单类
 *
 * @author kenfei
 * @since 2021-11-01 13:58:32
 */
@Data
public class ApproveTaskDto extends UserTaskParseHandler implements DtoConvert<ApproveTaskEntity> {
  /** id */
  private Long id;
  /** 任务类别 */
  private Long nodeId;
  /** 审批内容 */
  private String message;
  /** 任务ID */
  private String taskId;
  /** 表单数据 */
  @NotBlank
  private String formData;
  /** 指定审核人id */
  private Integer assigneeId;
}
