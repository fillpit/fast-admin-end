package com.kenfei.admin.bpmn.flowable.handler;

import com.kenfei.admin.bpmn.flowable.ActHistTaskQuery;
import org.flowable.engine.HistoryService;
import org.flowable.task.api.history.HistoricTaskInstance;
import org.flowable.task.api.history.HistoricTaskInstanceQuery;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 历史任务
 * @author fei
 * @date 2021/10/30 13:59
 */
@Component
public class HistTaskQueryHandler implements ActHistTaskQuery {
  protected static Logger logger = LoggerFactory.getLogger(HistTaskQueryHandler.class);

  @Autowired private HistoryService historyService;

  @Override
  public HistoricTaskInstanceQuery createHistoricTaskInstanceQuery() {
    return historyService.createHistoricTaskInstanceQuery();
  }

  @Override
  public HistoricTaskInstance activeTask(String instanceId) {
    return createHistoricTaskInstanceQuery().processInstanceId(instanceId).unfinished().singleResult();
  }

  @Override
  public HistoricTaskInstance finishedTask(String taskId) {
    return createHistoricTaskInstanceQuery().taskId(taskId).singleResult();
  }

  @Override
  public List<HistoricTaskInstance> listByInstanceId(String instanceId) {
    return createHistoricTaskInstanceQuery()
      .processInstanceId(instanceId).
      orderByTaskCreateTime()
      .desc()
      .list();

  }

  @Override
  public List<HistoricTaskInstance> pageListByInstanceId(String instanceId, int start, int limit) {
    return createHistoricTaskInstanceQuery()
      .processInstanceId(instanceId)
      .orderByTaskCreateTime()
      .desc().listPage(start, limit);

  }
}
