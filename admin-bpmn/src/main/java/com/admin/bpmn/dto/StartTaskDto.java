package com.admin.bpmn.dto;

import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 启动一个流程
 * @author fei
 * @date 2021/11/2 15:02
 */
@Data
public class StartTaskDto {
  /** 任务名称 */
  @NotBlank
  private String taskName;
  /** 任务类别 */
  private Boolean type;
  /** 流程key */
  @NotBlank
  private String processDefinitionKey;
  /** 表单数据 */
  @NotBlank
  private String formData;
}
