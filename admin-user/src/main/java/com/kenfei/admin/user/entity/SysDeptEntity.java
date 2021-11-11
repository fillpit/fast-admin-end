package com.kenfei.admin.user.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.kenfei.admin.core.common.base.AbstractEntity;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 部门信息表
 * @author fei
 * @date 2021/7/28 13:33
 */
@Entity
@Table(name="sys_dept")
public class SysDeptEntity extends AbstractEntity implements Serializable {
  /** 主键列*/
  private Long id;
  /** 角色 */
  private Set<SysRoleEntity> roles;
  /** 排序 */
  private Integer deptSort;
  /** 部门名称 */
  @NotBlank
  private String name;
  /** 是否启用 */
  @NotNull
  private Boolean enabled;

  /** 上级部门 */
  private Long parentId;

  /** 子菜单集合. */
  private List<SysDeptEntity> children;


  public SysDeptEntity() {}
  public SysDeptEntity(Long id) {
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

  @JsonIgnore
  @ManyToMany(mappedBy = "depts")
  public Set<SysRoleEntity> getRoles() {
    return roles;
  }

  public void setRoles(Set<SysRoleEntity> roles) {
    this.roles = roles;
  }

  @Column(name = "dept_sort")
  public Integer getDeptSort() {
    return deptSort;
  }

  public void setDeptSort(Integer deptSort) {
    this.deptSort = deptSort;
  }

  @Column(name = "name")
  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  @Column(name = "enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Column(name = "parent_id")
  public Long getParentId() {
    return parentId;
  }

  public void setParentId(Long parentId) {
    this.parentId = parentId;
  }

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @Transient
  public List<SysDeptEntity> getChildren() {
    return children;
  }

  public void setChildren(List<SysDeptEntity> children) {
    this.children = children;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    SysDeptEntity dept = (SysDeptEntity) o;
    return Objects.equals(getId(), dept.getId()) &&
      Objects.equals(name, dept.name);
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), name);
  }

}
