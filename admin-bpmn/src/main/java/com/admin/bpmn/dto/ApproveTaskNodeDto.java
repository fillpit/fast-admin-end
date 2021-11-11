package com.admin.bpmn.dto;

import com.admin.core.basic.DtoConvert;
import com.admin.bpmn.entity.ApproveTaskNodeEntity;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * 执行的流程节点(ApproveTaskNode)表单类
 *
 * @author kenfei
 * @since 2021-11-03 13:56:15
 */
@Data
public class ApproveTaskNodeDto implements DtoConvert<ApproveTaskNodeEntity> {

    /** id */

    private Long id;

    /** 受理人Id(接受任务的人) */

    private Long assigneeId;

    /** 受理人名称(接受任务的人) */

    private String assigneeName;

    /** 执行ID(task 的ID) */

    private String executionId;

    /** 当前执行节点的名称 */

    private String name;

    /** 流程的key */

    private String processDefKey;

    /** 流程实例的ID */

    private String processInstanceId;

    /** 流程节点的key */

    private String taskDefinitionKey;

    /** 表单数据 */

    private String formData;
}


