package com.kenfei.admin.bpmn.service.impl;

import com.kenfei.admin.auth.model.CurrentUser;
import com.kenfei.admin.bpmn.entity.ApproveTaskNodeEntity;
import com.kenfei.admin.bpmn.flowable.ActInstance;
import com.kenfei.admin.bpmn.flowable.ActTask;
import com.kenfei.admin.bpmn.model.TaskVO;
import com.kenfei.admin.bpmn.service.ApproveTaskNodeService;
import com.kenfei.admin.core.common.base.AbstractServiceImpl;
import com.kenfei.admin.bpmn.entity.ApproveTaskEntity;
import com.kenfei.admin.bpmn.repository.ApproveTaskRepository;
import com.kenfei.admin.bpmn.service.ApproveTaskService;
import org.flowable.engine.runtime.ProcessInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.*;

/**
 * 激活的任务(ApproveTask)表服务实现类
 *
 * @author kenfei
 * @since 2021-11-01 13:58:32
 */
@Service("approveTaskService")
public class ApproveTaskServiceImpl extends AbstractServiceImpl<ApproveTaskEntity, Long>
    implements ApproveTaskService {
  private final ApproveTaskRepository approveTaskRepository;

  @Autowired private ActInstance actInstance;
  @Autowired private ActTask actTask;
  @Autowired private ApproveTaskNodeService approveTaskNodeService;

  @Autowired
  public ApproveTaskServiceImpl(ApproveTaskRepository approveTaskRepository) {
    super(approveTaskRepository);
    this.approveTaskRepository = approveTaskRepository;
  }

  @Override
  public Page<ApproveTaskEntity> index(Pageable pageable) {
    return approveTaskRepository.findAll(
        (root, cq, cb) -> {
          List<Predicate> list = new LinkedList<>();

          return cq.where(list.toArray(new Predicate[0])).getRestriction();
        },
        pageable);
  }

  @Override
  public Page<ApproveTaskEntity> findOriginator(Pageable pageable) {
    Long userId = CurrentUser.userId();
    return approveTaskRepository.findOriginator(userId, pageable);
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApproveTaskEntity start(String processDefinitionKey, String taskName, String formData)
      throws Exception {

    ProcessInstance processInstance = actInstance.startProcessInstanceByKey(processDefinitionKey);

    // 保存流程信息
    ApproveTaskEntity approveTask = new ApproveTaskEntity();
    approveTask.setTaskName(taskName);
    approveTask.setUserId(CurrentUser.userId());
    approveTask.setUserName(CurrentUser.userName().orElse(""));
    approveTask.setStatus(true);
    approveTask.setType(true);
    approveTask.setModuleId(1);
    approveTask.setProcessDefinitionKey(processInstance.getProcessDefinitionKey());
    approveTask.setProcessInstanceId(processInstance.getProcessInstanceId());
    approveTask.setProcessName(processInstance.getProcessDefinitionName());
    approveTask = this.save(approveTask);

    // 生成节点信息节点
    approveTaskNodeService.startNode(
        approveTask.getId(), processInstance.getProcessInstanceId(), formData);

    return approveTask;
  }

  @Override
  @Transactional(rollbackFor = Exception.class)
  public ApproveTaskEntity execute(
      Long id, Long nodeId, String taskId, String formData, String message) throws Exception {
    Map<String, Object> variables = new HashMap<>();
    variables.put("outcome", message);
    Map<String, TaskVO> map = actTask.complete(taskId, variables, false);

    TaskVO finish = map.get("finish");
    ApproveTaskNodeEntity node =
        approveTaskNodeService.complete(nodeId, finish.getProcessInstanceId(), formData, message);

    // 判断还有没有下一个节点
    ApproveTaskEntity approveTask = this.findById(id);
    if (Objects.isNull(node)) {
      approveTask.setStatus(true);
      this.update(approveTask);
    }

    return approveTask;
  }

  /**
   * 查询公共条件
   *
   * @return 条件构造对象
   */
  private Specification<ApproveTaskEntity> findWhere() {
    return (root, cq, cb) -> {
      List<Predicate> list = new LinkedList<>();

      return cq.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }
}
