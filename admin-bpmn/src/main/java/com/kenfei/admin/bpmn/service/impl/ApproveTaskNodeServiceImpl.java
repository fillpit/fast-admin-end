package com.kenfei.admin.bpmn.service.impl;

import com.kenfei.admin.auth.config.security.JwtUser;
import com.kenfei.admin.auth.model.CurrentUser;
import com.kenfei.admin.bpmn.common.emun.ApproveStateEnum;
import com.kenfei.admin.bpmn.flowable.ActTaskQuery;
import com.kenfei.admin.core.common.base.AbstractServiceImpl;
import com.kenfei.admin.bpmn.entity.ApproveTaskNodeEntity;
import com.kenfei.admin.bpmn.repository.ApproveTaskNodeRepository;
import com.kenfei.admin.bpmn.service.ApproveTaskNodeService;
import lombok.Data;
import org.flowable.task.api.Task;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.persistence.criteria.Predicate;
import java.util.LinkedList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

/**
 * 执行的流程节点(ApproveTaskNode)表服务实现类
 *
 * @author kenfei
 * @since 2021-11-03 13:56:15
 */
@Service("approveTaskNodeService")
public class ApproveTaskNodeServiceImpl extends AbstractServiceImpl<ApproveTaskNodeEntity, Long>
    implements ApproveTaskNodeService {
  private final ApproveTaskNodeRepository approveTaskNodeRepository;

  @Autowired private ActTaskQuery actTaskQuery;

  @Autowired
  public ApproveTaskNodeServiceImpl(ApproveTaskNodeRepository approveTaskNodeRepository) {
    super(approveTaskNodeRepository);
    this.approveTaskNodeRepository = approveTaskNodeRepository;
  }

  @Override
  public Page<ApproveTaskNodeEntity> todoList(Pageable pageable) {
    TodoVo vo = getUserParams();
    return approveTaskNodeRepository.findApproveTaskList(
      vo.getUserId(), vo.getRoleId(), vo.getJobsIds(), vo.getDeptId(), 0, pageable);
  }

  @Override
  public Long countTodoList() {
    TodoVo vo = getUserParams();

    return approveTaskNodeRepository.countApproveTaskList(
      vo.getUserId(), vo.getRoleId(), vo.getJobsIds(), vo.getDeptId());
  }

  @Override
  public Page<ApproveTaskNodeEntity> done(Pageable pageable) {
    TodoVo vo = getUserParams();

    return approveTaskNodeRepository.findApproveTaskList(
      vo.getUserId(), vo.getRoleId(), vo.getJobsIds(), vo.getDeptId(), 1, pageable);
  }

  private TodoVo getUserParams() {
    JwtUser user = CurrentUser.user();
    Long userId = user.getId();
    Long deptId = Optional.ofNullable(user.getDept()).map(JwtUser.JwtDept::getId).orElse(-1L);
    JwtUser.JwtDept dept = user.getDept();
    List<Long> jobsIds =
      user.getJobs().parallelStream()
        .map(JwtUser.JwtJobs::getId)
        .distinct()
        .collect(Collectors.toList());
    List<Long> roleIds =
      user.getRoles().parallelStream()
        .map(JwtUser.JwtRole::getRoleId)
        .distinct()
        .collect(Collectors.toList());
    if (jobsIds.isEmpty()) {
      jobsIds.add(-9999L);
    }
    if (roleIds.isEmpty()) {
      roleIds.add(-9999L);
    }

    TodoVo vo = new TodoVo();
    vo.setUserId(userId);
    vo.setDeptId(deptId);
    vo.setJobsIds(jobsIds);
    vo.setRoleId(roleIds);

    return vo;
  }

  @Override
  @Transactional
  public ApproveTaskNodeEntity startNode(
      Long approveTaskId, String processInstanceId, String formData) {
    ApproveTaskNodeEntity node = new ApproveTaskNodeEntity();
    node.setApproveTaskId(approveTaskId);
    node.setAssigneeId(CurrentUser.userId());
    node.setAssigneeName(CurrentUser.userName().orElse(""));
    node.setExecutionId("");
    node.setName("提交申请");
    node.setTaskDefinitionKey("");
    node.setFormData(formData);
    node.setUserId(CurrentUser.userId());
    node.setUserName(CurrentUser.userName().orElse(""));
    node.setApproveId(CurrentUser.userId());
    node.setApproveName(CurrentUser.userName().orElse(""));
    node.setStatus(true);
    node.setApproveState(ApproveStateEnum.START);

    node = this.save(node);

    // 生成下一个处理节点
    this.activeNode(approveTaskId, processInstanceId, formData);

    return node;
  }

  /**
   * 下一个节点
   *
   * @param approveTaskId 主表ID
   * @param processInstanceId 流程ID
   * @param formData 表单数据
   * @return /
   */
  @Transactional(rollbackFor = Exception.class)
  ApproveTaskNodeEntity activeNode(
      Long approveTaskId,
      String processInstanceId,
      String formData) {
    Task task = actTaskQuery.processInstanceId(processInstanceId);

    // 没有下一个节点ID就表面到了结束节点了
    if (Objects.isNull(task)) {
      // 生成结束节点信息
      this.endNode(approveTaskId, formData);
      return null;
    }

    ApproveTaskNodeEntity node = new ApproveTaskNodeEntity();
    node.setApproveTaskId(approveTaskId);
    node.setAssigneeId(CurrentUser.userId());
    node.setAssigneeName(CurrentUser.userName().orElse(""));
    node.setExecutionId(task.getId());
    node.setName(task.getName());
    node.setTaskDefinitionKey(task.getTaskDefinitionKey());
    node.setFormData(formData);
    node.setUserId(CurrentUser.userId());
    node.setUserName(CurrentUser.userName().orElse(""));
    // 待办人
    node.setCandidateApproveUserId(CurrentUser.userId());

    //    node.setApproveId(CurrentUser.userId());
    //    node.setApproveName(CurrentUser.userName().orElse(""));
    node.setStatus(false);

    return this.save(node);
  }

  @Override
  public ApproveTaskNodeEntity complete(
      Long id, String processInstanceId, String formData, String message) {
    // 更新上一个节点信息
    ApproveTaskNodeEntity node = this.findById(id);
    node.setApproveId(CurrentUser.userId());
    node.setApproveName(CurrentUser.user().getUsername());
    node.setFormData(formData);
    node.setStatus(true);
    node.setApproveState(ApproveStateEnum.COMPLETE);
    this.update(node);

    // 生成当前激活节点
    return this.activeNode(
        node.getApproveTaskId(), processInstanceId, formData);
  }

  /**
   * 结束节点
   *
   * @param approveTaskId 主表ID
   * @param formData 表单数据
   * @return /
   */
  private ApproveTaskNodeEntity endNode(Long approveTaskId, String formData) {
    ApproveTaskNodeEntity node = new ApproveTaskNodeEntity();
    node.setAssigneeId(CurrentUser.userId());
    node.setAssigneeName(CurrentUser.userName().orElse(""));
    node.setExecutionId("");
    node.setName("完成");
    node.setTaskDefinitionKey("");
    node.setFormData(formData);
    node.setUserId(CurrentUser.userId());
    node.setUserName(CurrentUser.userName().orElse(""));
    node.setApproveId(CurrentUser.userId());
    node.setApproveName(CurrentUser.userName().orElse(""));
    node.setStatus(true);
    node.setApproveTaskId(approveTaskId);
    node.setApproveState(ApproveStateEnum.END);

    return this.save(node);
  }

  /**
   * 查询公共条件
   *
   * @return 条件构造对象
   */
  private Specification<ApproveTaskNodeEntity> findWhere() {
    return (root, cq, cb) -> {
      List<Predicate> list = new LinkedList<>();

      return cq.where(list.toArray(new Predicate[0])).getRestriction();
    };
  }

  @Data
  static class TodoVo {
    /** 用户ID */
    private Long userId;
    /** 部门ID */
    private Long deptId;
    /** 角色ID */
    private List<Long> roleId;
    /** 岗位ID */
    private List<Long> jobsIds;
  }
}
