package com.admin.bpmn.service;

import com.admin.bpmn.common.emun.ApproveStateEnum;
import com.admin.bpmn.entity.ApproveTaskEntity;
import com.admin.bpmn.entity.ApproveTaskNodeEntity;
import com.admin.core.basic.InterfaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 执行的流程节点(ApproveTaskNode)表服务接口
 *
 * @author kenfei
 * @since 2021-11-03 13:56:15
 */
public interface ApproveTaskNodeService extends InterfaceService<ApproveTaskNodeEntity, Long> {

  /**
   * 获取待办列表
   * @param pageable 分页
   * @return 待办列表
   */
  Page<ApproveTaskNodeEntity> todoList(Pageable pageable);

  /**
   * 统计待办的数量
   * @return 待办数量
   */
  Long countTodoList();

  /**
   * 已办列表
   * @param pageable 分页
   * @return /
   */
  Page<ApproveTaskNodeEntity> done(Pageable pageable);

  /**
   * 开始节点（提交申请）
   * @param approveTaskId 主表ID
   * @param processInstanceId 流程ID
   * @param formData 表单数据
   * @return /
   */
  ApproveTaskNodeEntity startNode(Long approveTaskId, String processInstanceId, String formData);

  /**
   * 执行任务
   * @param id ID
   * @param processInstanceId 流程实例ID
   * @param formData 表单数据
   * @param message
   * @return /
   */
  ApproveTaskNodeEntity complete(Long id, String processInstanceId, String formData, String message);
}


