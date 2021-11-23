package com.kenfei.admin.modules.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kenfei.admin.core.common.base.AbstractEntity;

import javax.persistence.*;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;

/**
 * (SysOrg)实体类
 *
 * @author kenfei
 * @since 2021-11-20 15:51:12
 */
@Entity
@Table(name = "sys_org")
public class SysOrgEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = -92141617697906764L;
  private Long id;
  /** 排序 */
  private Integer deptSort;
  /** 状态 */
  private Boolean enabled;
  /** 编码 */
  private String code;
  /** 全名称 */
  private String name;
  /** 简称 */
  private String shortName;
  /** 描述 */
  private String descr;
  /** 父ID */
  private Long parentId;
  /** 子节点的个数 */
  private Integer subCount;

  private List<SysOrgEntity> children;

  public SysOrgEntity(){}

  public SysOrgEntity(Long id) {
    this.id = id;
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
  @Column(name = "dept_sort")
  public Integer getDeptSort() {
    return deptSort;
  }

  public void setDeptSort(Integer deptSort) {
    this.deptSort = deptSort;
  }

  @Basic
  @Column(name = "enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Basic
  @Column(name = "code")
  public String getCode() {
    return code;
  }

  public void setCode(String code) {
    this.code = code;
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
  @Column(name = "short_name")
  public String getShortName() {
    return shortName;
  }

  public void setShortName(String shortName) {
    this.shortName = shortName;
  }

  @Basic
  @Column(name = "descr")
  public String getDescr() {
    return descr;
  }

  public void setDescr(String descr) {
    this.descr = descr;
  }

  @Basic
  @Column(name = "parent_id")
  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @Basic
  @Column(name = "sub_count")
  public Integer getSubCount() {
    return subCount;
  }

  public void setSubCount(Integer subCount) {
    this.subCount = subCount;
  }

  @Transient
  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  public List<SysOrgEntity> getChildren() {
    return children;
  }

  public void setChildren(List<SysOrgEntity> children) {
    this.children = children;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (!(o instanceof SysOrgEntity)) return false;
    SysOrgEntity that = (SysOrgEntity) o;
    return Objects.equals(id, that.id) && Objects.equals(deptSort, that.deptSort) && Objects.equals(enabled, that.enabled) && Objects.equals(code, that.code) && Objects.equals(name, that.name) && Objects.equals(shortName, that.shortName) && Objects.equals(descr, that.descr) && Objects.equals(parentId, that.parentId) && Objects.equals(subCount, that.subCount) && Objects.equals(children, that.children);
  }

  @Override
  public int hashCode() {
    return Objects.hash(id, deptSort, enabled, code, name, shortName, descr, parentId, subCount, children);
  }
}
