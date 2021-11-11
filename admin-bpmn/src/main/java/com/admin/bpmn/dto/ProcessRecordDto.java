package com.admin.bpmn.dto;

import com.admin.core.basic.DtoConvert;
import com.admin.bpmn.entity.ProcessRecordEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;

/**
 * (TbProcessRecord)表单类
 *
 * @author kenfei
 * @since 2021-10-28 16:34:33
 */
@Data
public class ProcessRecordDto implements DtoConvert<ProcessRecordEntity> {

  /** 流程ID */
  @NotBlank
  private String procId;
  /** 流程名称 */
  @NotBlank
  private String name;
  /** 流程xml */
  @NotBlank
  private String xmlStr;
  /** svg码 */
  @NotBlank
  private String svgStr;
  /** 描述 */
  private String note;
}
