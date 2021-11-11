package com.admin.bpmn.entity;

import com.admin.core.basic.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 激活的任务(ApproveTask)实体类
 *
 * @author kenfei
 * @since 2021-11-01 15:55:23
 */
@Entity
@Table(name = "tb_approve_task")
public class ApproveTaskEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = 290890480035928255L;

  /** 自增主键 */
  private Long id;
  /** 任务名称 */
  private String taskName;
  /** 用户id */
  private Long userId;
  /** 用户名字(申请者名字) */
  private String userName;
  /** 状态 */
  private Boolean status;
  /** 任务类别 */
  private Boolean type;
  /** 模块ID */
  private Integer moduleId;
  /** 流程实例的ID */
  private String processInstanceId;
  /** 流程key */
  private String processDefinitionKey;
  /** 流程名 */
  private String processName;
  /** 删除状态 */
  private Boolean delFlag;
  /** 激活的节点 */
  private Set<ApproveTaskNodeEntity> nodes;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "task_name")
  public String getTaskName() {
    return taskName;
  }

  public void setTaskName(String taskName) {
    this.taskName = taskName;
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
  @Column(name = "status")
  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Basic
  @Column(name = "type")
  public Boolean getType() {
    return type;
  }

  public void setType(Boolean type) {
    this.type = type;
  }

  @Basic
  @Column(name = "module_id")
  public Integer getModuleId() {
    return moduleId;
  }

  public void setModuleId(Integer moduleId) {
    this.moduleId = moduleId;
  }

  @Basic
  @Column(name = "process_definition_key")
  public String getProcessDefinitionKey() {
    return processDefinitionKey;
  }

  public void setProcessDefinitionKey(String processDefinitionKey) {
    this.processDefinitionKey = processDefinitionKey;
  }

  @Basic
  @Column(name = "del_flag")
  public Boolean getDelFlag() {
    return delFlag;
  }

  public void setDelFlag(Boolean delFlag) {
    this.delFlag = delFlag;
  }

  @Basic
  @Column(name = "process_instance_id")
  public String getProcessInstanceId() {
    return processInstanceId;
  }

  public void setProcessInstanceId(String processInstanceId) {
    this.processInstanceId = processInstanceId;
  }

  @Basic
  @Column(name = "process_name")
  public String getProcessName() {
    return processName;
  }

  public void setProcessName(String processName) {
    this.processName = processName;
  }

  @JsonIgnore
  @OneToMany(
      mappedBy = "task",
      cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
  public Set<ApproveTaskNodeEntity> getNodes() {
    return nodes;
  }

  public void setNodes(Set<ApproveTaskNodeEntity> nodes) {
    this.nodes = nodes;
  }
}
