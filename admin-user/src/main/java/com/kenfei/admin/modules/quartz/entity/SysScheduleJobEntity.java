package com.kenfei.admin.modules.quartz.entity;

import com.kenfei.admin.core.common.base.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;

/**
 * (SysJob)实体类
 *
 * @author fei
 * @since 2020-10-28 10:22:37
 */
@Entity
@Table(name = "sys_schedule_job")
public class SysScheduleJobEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = -25693015846800948L;
  /** 主键列*/
  private Long id;
  /** 执行表达式 */
  private String cronExpression;
  /** bean 名称*/
  private String beanName;
  /** 方法名*/
  private String methodName;
  /** 参数 */
  private String params;

  /** 任务名称 */
  private String jobName;
  /** 描述 */
  private String remark;
  /** 任务状态 */
  private Boolean status;

  /** 计划策略(0=默认,1=立即触发执行,2=触发一次执行,3=不触发立即执行) */
  private Integer misfirePolicy;
  /** 并发执行(0=允许,1=禁止) */
  private Boolean concurrent;

  /** 负责人 */
  private String personInCharge;
  /** 报警邮箱 */
  private String email;

  @Id
  @Column(name = "id")
  @GeneratedValue(strategy = GenerationType.AUTO)
  public Long getId() {
    return id;
  }

  public void setId(Long id) {
    this.id = id;
  }

  @Basic
  @Column(name = "bean_name")
  public String getBeanName() {
    return beanName;
  }

  public void setBeanName(String beanName) {
    this.beanName = beanName;
  }

  @Basic
  @Column(name = "cron_expression")
  public String getCronExpression() {
    return cronExpression;
  }

  public void setCronExpression(String cronExpression) {
    this.cronExpression = cronExpression;
  }

  @Basic
  @Column(name = "method_name")
  public String getMethodName() {
    return methodName;
  }

  public void setMethodName(String methodName) {
    this.methodName = methodName;
  }

  @Basic
  @Column(name = "params")
  public String getParams() {
    return params;
  }

  public void setParams(String params) {
    this.params = params;
  }

  @Basic
  @Column(name = "remark")
  public String getRemark() {
    return remark;
  }

  public void setRemark(String remark) {
    this.remark = remark;
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
  @Column(name = "job_name")
  public String getJobName() {
    return jobName;
  }

  public void setJobName(String jobName) {
    this.jobName = jobName;
  }

  @Basic
  @Column(name = "misfire_Policy")
  public Integer getMisfirePolicy() {
    return misfirePolicy;
  }

  public void setMisfirePolicy(Integer misfirePolicy) {
    this.misfirePolicy = misfirePolicy;
  }

  @Basic
  @Column(name = "concurrent")
  public Boolean getConcurrent() {
    return concurrent;
  }

  public void setConcurrent(Boolean concurrent) {
    this.concurrent = concurrent;
  }

  @Basic
  @Column(name = "person_in_charge")
  public String getPersonInCharge() {
    return personInCharge;
  }

  public void setPersonInCharge(String personInCharge) {
    this.personInCharge = personInCharge;
  }

  @Basic
  @Column(name = "email")
  public String getEmail() {
    return email;
  }

  public void setEmail(String email) {
    this.email = email;
  }
}
