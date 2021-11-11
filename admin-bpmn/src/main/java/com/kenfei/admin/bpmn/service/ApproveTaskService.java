package com.kenfei.admin.bpmn.service;

import com.kenfei.admin.bpmn.entity.ApproveTaskEntity;
import com.kenfei.admin.core.common.base.InterfaceService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

/**
 * 激活的任务(ApproveTask)表服务接口
 *
 * @author kenfei
 * @since 2021-11-01 13:58:32
 */
public interface ApproveTaskService extends InterfaceService<ApproveTaskEntity, Long> {
  /**
   * 主页的列表数据
   * @param pageable 分页参数
   * @return 数据分页
   */
  Page<ApproveTaskEntity> index(Pageable pageable);

  /**
   * 获取发起人相关的列表
   * @param pageable 分页
   * @return /
   */
  Page<ApproveTaskEntity> findOriginator(Pageable pageable);

  /**
   * 启动一个任务
   * @param processDefinitionKey 流程key
   * @param taskName 任务名称
   * @param formData 表单数据
   * @return /
   */
  ApproveTaskEntity start(String processDefinitionKey, String taskName, String formData) throws Exception;

  /**
   * 执行任务
   * @param id ID
   * @param nodeId 节点ID
   * @param taskId 任务ID
   * @param formData 表单数据
   * @param message 批注
   * @return /
   */
  ApproveTaskEntity execute(Long id, Long nodeId, String taskId, String formData, String message) throws Exception;
}


