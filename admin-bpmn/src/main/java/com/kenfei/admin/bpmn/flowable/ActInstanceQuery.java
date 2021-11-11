package com.kenfei.admin.bpmn.flowable;

import org.flowable.engine.runtime.ProcessInstance;
import org.flowable.engine.runtime.ProcessInstanceQuery;

/**
 * 流程实例查询相关封装
 * @author fei
 * @date 2021/10/30 13:52
 */
 public interface ActInstanceQuery {
  /**
   * 流程实例查询对象
   *
   * @return
   */
   ProcessInstanceQuery createProcessInstanceQuery();

  /**
   * 查询流程实例
   *
   * @param processInstanceId 流程实例标识
   * @return
   */
   ProcessInstance processInstanceId(String processInstanceId);

  /**
   * 查询流程实例
   *
   * @param processInstanceBusinessKey 流程实例业务键名
   * @return
   */
   ProcessInstance processInstanceBusinessKey(String processInstanceBusinessKey);

  /**
   * 查询流程实例
   *
   * @param taskId 流程任务标识
   * @return
   */
   ProcessInstance taskId(String taskId);


  /**
   * 判断流程实例是否已结束
   *
   * @param processInstanceId 流程实例标识
   * @return
   */
   boolean hasProcessInstanceFinished(String processInstanceId);
}
