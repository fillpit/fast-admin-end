package com.admin.quartz.entity;

import com.admin.core.basic.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * (SysJobLog)实体类
 *
 * @author fei
 * @since 2020-10-28 10:23:03
 */
@Entity
@Table(name = "sys_schedule_job_log")
public class SysScheduleJobLogEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = -74792887196541183L;
  /** 主键列*/
  private Long id;
  private String beanName;

  private String error;

  private Long jobId;

  private String methodName;

  private String params;

  private Boolean status;

  private Integer times;
  /** 任务名称 */
  private String jobName;
  /** 任务分组 */
  private String jobGroup;
  /** 调用目标字符串 */
  private String invokeTarget;
  /** 日志信息 */
  private String jobMessage;
  /** 异常信息 */
  private String exceptionInfo;
  /** 开始时间 */
  private Date startTime;
  /** 结束时间 */
  private Date endTime;

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
  @Column(name = "error")
  public String getError() {
    return error;
  }

  public void setError(String error) {
    this.error = error;
  }

  @Basic
  @Column(name = "job_id")
  public Long getJobId() {
    return jobId;
  }

  public void setJobId(Long jobId) {
    this.jobId = jobId;
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
  @Column(name = "status")
  public Boolean getStatus() {
    return status;
  }

  public void setStatus(Boolean status) {
    this.status = status;
  }

  @Basic
  @Column(name = "times")
  public Integer getTimes() {
    return times;
  }

  public void setTimes(Integer times) {
    this.times = times;
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
  @Column(name = "job_group")
  public String getJobGroup() {
    return jobGroup;
  }

  public void setJobGroup(String jobGroup) {
    this.jobGroup = jobGroup;
  }

  @Basic
  @Column(name = "Invoke_target")
  public String getInvokeTarget() {
    return invokeTarget;
  }

  public void setInvokeTarget(String invokeTarget) {
    this.invokeTarget = invokeTarget;
  }

  @Basic
  @Column(name = "job_message")
  public String getJobMessage() {
    return jobMessage;
  }

  public void setJobMessage(String jobMessage) {
    this.jobMessage = jobMessage;
  }

  @Basic
  @Column(name = "exception_info")
  public String getExceptionInfo() {
    return exceptionInfo;
  }

  public void setExceptionInfo(String exceptionInfo) {
    this.exceptionInfo = exceptionInfo;
  }

  @Basic
  @Column(name = "start_time")
  public Date getStartTime() {
    return startTime;
  }

  public void setStartTime(Date startTime) {
    this.startTime = startTime;
  }

  @Basic
  @Column(name = "end_time")
  public Date getEndTime() {
    return endTime;
  }

  public void setEndTime(Date endTime) {
    this.endTime = endTime;
  }
}
