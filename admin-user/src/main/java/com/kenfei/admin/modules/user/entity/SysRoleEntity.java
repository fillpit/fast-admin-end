package com.kenfei.admin.modules.user.entity;

import com.kenfei.admin.core.common.base.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Set;

/**
 * 系统角色映射类.
 *
 * @author fei
 * @date 2017/10/3
 */
@Entity
@Table(name = "sys_role", schema = "fastAdmin")
public class SysRoleEntity extends AbstractEntity implements Serializable {
  private static final long serialVersionUID = 2230732982891238618L;
  /** 主键列*/
  private Long id;
  /** 角色名. */
  private String name;
  /** 权限级别，低级别的无法编辑高级别的 */
  private Integer level;
  /** 数据范围 */
  private String dataScope;
  /** 描述. */
  private String description;
  /** 状态 */
  private Boolean enabled;
  /** 菜单ID集合. */
  private Set<Long> menuIds;
  /** 角色权限集合 */
  private Set<SysMenuEntity> menus;
  /** 部门 */
  private Set<SysDeptEntity> depts;

  public SysRoleEntity() {}
  public SysRoleEntity(Long id) {
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
  @Column(name = "description")
  public String getDescription() {
    return description;
  }

  public void setDescription(String description) {
    this.description = description;
  }

  @Basic
  @Column(name = "enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @JsonIgnore
  @ElementCollection
  @CollectionTable(
      name = "sys_roles_menus",
      joinColumns = {@JoinColumn(name = "role_id")})
  @Column(name = "menu_id")
  public Set<Long> getMenuIds() {
    return menuIds;
  }

  public void setMenuIds(Set<Long> menuIds) {
    this.menuIds = menuIds;
  }

  @ManyToMany(fetch = FetchType.EAGER)
  @JoinTable(
      name = "sys_roles_menus",
      joinColumns = {@JoinColumn(name = "role_id", referencedColumnName = "id")},
      inverseJoinColumns = {@JoinColumn(name = "menu_id", referencedColumnName = "id")})
  public Set<SysMenuEntity> getMenus() {
    return menus;
  }

  public void setMenus(Set<SysMenuEntity> menus) {
    this.menus = menus;
  }

  @ManyToMany
  @JoinTable(name = "sys_roles_depts",
    joinColumns = {@JoinColumn(name = "role_id",referencedColumnName = "id")},
    inverseJoinColumns = {@JoinColumn(name = "dept_id",referencedColumnName = "id")})
  public Set<SysDeptEntity> getDepts() {
    return depts;
  }

  public void setDepts(Set<SysDeptEntity> depts) {
    this.depts = depts;
  }

  @Basic
  @Column(name = "level")
  public Integer getLevel() {
    return level;
  }

  public void setLevel(Integer level) {
    this.level = level;
  }

  @Basic
  @Column(name = "data_scope")
  public String getDataScope() {
    return dataScope;
  }

  public void setDataScope(String dataScope) {
    this.dataScope = dataScope;
  }


}
