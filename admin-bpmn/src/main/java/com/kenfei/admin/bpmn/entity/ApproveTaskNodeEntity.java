package com.kenfei.admin.bpmn.entity;

import com.kenfei.admin.bpmn.common.emun.ApproveStateEnum;
import com.kenfei.admin.core.common.base.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * 执行的流程节点(ApproveTaskNode)实体类
 *
 * @author kenfei
 * @since 2021-11-03 14:16:49
 */
@Entity
@Table(name = "tb_approve_task_node")
public class ApproveTaskNodeEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = -99313422858650690L;
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
  /** 流程节点的key */
  private String taskDefinitionKey;
  /** 表单数据 */
  private String formData;
  /** 申请人(上一个节点的的操作人) */
  private Long userId;
  /** 申请人姓名 */
  private String userName;
  /** 审核人ID */
  private Long approveId;
  /** 审核人名称 */
  private String approveName;
  /** 状态 */
  private Boolean status;
  /** 候选审核人 */
  private Long candidateApproveUserId;
  /** 候选审核人角色 */
  private Long candidateApproveRoleId;
  /** 候选审核人岗位 */
  private Long candidateApproveJobsId;
  /** 候选审核人部门 */
  private Long candidateApproveDeptId;

  /** 审批流程表id */
  private Long approveTaskId;
  /** 审批状态 */
  private ApproveStateEnum approveState;

  /** 流程对象 */
  private ApproveTaskEntity task;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return this.id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "assignee_id")
  public Long getAssigneeId() {
    return assigneeId;
  }

  public void setAssigneeId(Long assigneeId) {
    this.assigneeId = assigneeId;
  }

  @Basic
  @Column(name = "assignee_name")
  public String getAssigneeName() {
    return assigneeName;
  }

  public void setAssigneeName(String assigneeName) {
    this.assigneeName = assigneeName;
  }

  @Basic
  @Column(name = "execution_id")
  public String getExecutionId() {
    return executionId;
  }

  public void setExecutionId(String executionId) {
    this.executionId = executionId;
  }

  @Basic
  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "task_definition_key")
  public String getTaskDefinitionKey() {
    return taskDefinitionKey;
  }

  public void setTaskDefinitionKey(String taskDefinitionKey) {
    this.taskDefinitionKey = taskDefinitionKey;
  }

  @Basic
  @Column(name = "form_data")
  public String getFormData() {
    return formData;
  }

  public void setFormData(String formData) {
    this.formData = formData;
  }

  @Basic
  @Column(name = "approve_id")
  public Long getApproveId() {
    return approveId;
  }

  public void setApproveId(Long approveId) {
    this.approveId = approveId;
  }

  @Basic
  @Column(name = "user_id")
  public Long getUserId() {
    return userId;
  }

  public void setUserId(Long userId) {
    this.userId = userId;
  }

  @Basic
  @Column(name = "user_name")
  public String getUserName() {
    return userName;
  }

  public void setUserName(String userName) {
    this.userName = userName;
  }

  @Basic
  @Column(name = "approve_name")
  public String getApproveName() {
    return approveName;
  }

  public void setApproveName(String approveName) {
    this.approveName = approveName;
  }

  @Basic
  @Column(name = "approve_task_id")
  public Long getApproveTaskId() {
    return approveTaskId;
  }

  public void setApproveTaskId(Long approveTaskId) {
    this.approveTaskId = approveTaskId;
  }

  @Basic
  @Column(name = "status")
  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Basic
  @Column(name = "candidate_approve_user_id")
  public Long getCandidateApproveUserId() {
    return candidateApproveUserId;
  }

  public void setCandidateApproveUserId(Long candidateApproveUserId) {
    this.candidateApproveUserId = candidateApproveUserId;
  }

  @Basic
  @Column(name = "candidate_approve_role_id")
  public Long getCandidateApproveRoleId() {
    return candidateApproveRoleId;
  }

  public void setCandidateApproveRoleId(Long candidateApproveRoleId) {
    this.candidateApproveRoleId = candidateApproveRoleId;
  }

  @Basic
  @Column(name = "candidate_approve_jobs_id")
  public Long getCandidateApproveJobsId() {
    return candidateApproveJobsId;
  }

  public void setCandidateApproveJobsId(Long candidateApproveJobsId) {
    this.candidateApproveJobsId = candidateApproveJobsId;
  }

  @Basic
  @Column(name = "candidate_approve_dept_id")
  public Long getCandidateApproveDeptId() {
    return candidateApproveDeptId;
  }

  public void setCandidateApproveDeptId(Long candidateApproveDeptId) {
    this.candidateApproveDeptId = candidateApproveDeptId;
  }

  @JoinColumn(name = "approve_task_id", insertable = false, updatable = false)
  @ManyToOne(fetch = FetchType.EAGER)
  public ApproveTaskEntity getTask() {
    return task;
  }

  public void setTask(ApproveTaskEntity task) {
    this.task = task;
  }

  @Basic
  @Enumerated(value = EnumType.ORDINAL)
  @Column(name = "approve_state")
  public ApproveStateEnum getApproveState() {
    return approveState;
  }

  public void setApproveState(ApproveStateEnum approveState) {
    this.approveState = approveState;
  }
}
