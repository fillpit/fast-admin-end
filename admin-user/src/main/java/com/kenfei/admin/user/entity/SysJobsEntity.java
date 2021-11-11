package com.kenfei.admin.user.entity;

import com.kenfei.admin.core.common.base.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Objects;

/**
 * 岗位信息表
 * @author fei
 * @date 2021/7/28 13:57
 */
@Entity
@Table(name = "sys_jobs")
public class SysJobsEntity extends AbstractEntity implements Serializable {
  /** 主键列*/
  private Long id;
  /** 岗位名称 */
  @NotBlank
  private String name;
  /** 岗位排序 */
  @NotNull
  private Long jobSort;
  /** 是否启用 */
  @NotNull
  private Boolean enabled;

  public SysJobsEntity() {}
  public SysJobsEntity(Long id) {
    this.setId(id);
  }

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
  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Basic
  @Column(name = "job_sort")
  public Long getJobSort() {
    return jobSort;
  }

  public void setJobSort(Long jobSort) {
    this.jobSort = jobSort;
  }

  @Basic
  @Column(name = "enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SysJobsEntity job = (SysJobsEntity) o;
    return Objects.equals(getId(), job.getId());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId());
  }
}
