package com.kenfei.admin.user.entity;

import com.kenfei.admin.core.common.base.AbstractEntity;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;

import javax.persistence.*;
import java.util.List;
import java.util.Objects;
import java.util.Set;

/**
 * 菜单对应实体.
 *
 * @author fei
 * @since 2019-01-02
 */
@Entity
@Table(name = "sys_menu", schema = "fastAdmin")
public class SysMenuEntity extends AbstractEntity {
  /** 主键列*/
  private Long id;
  /** 菜单名称(路由名称). */
  private String name;
  /** 菜单名称. */
  private String title;
  /** 图标 */
  private String icon;
  /** uri 路径. */
  private String path;
  /** 组件名. */
  private String component;
  /** 是否需要缓存. */
  private Boolean keepAlive;
  /** 用于隐藏不需要在菜单中展示的子路由。用法可以查看 个人设置 的配置。(一代版本遗留) */
  private Boolean hideChildrenInMenu;
  /** 可以在菜单中不展示这个路由，包括子路由。效果可以查看 other 下的路由配置。(一代版本遗留) */
  private Boolean hidden;
  /** 可以强制当前页面不显示 PageHeader 组件中的页面带的 面包屑和页面标题栏 */
  private Boolean hiddenHeaderContent;
  /** 菜单重定向（目录菜单可用） */
  private String redirect;
  /** 上级菜单ID. */
  private Long parentId;
  /** 是否启用. */
  private Boolean enabled;
  /** 菜单排序 */
  private Integer menuSort;

  /** 权限集合(显示用). */
  private List<SysMenuEntity> permissions;
  /** 子菜单集合(显示用). */
  private List<SysMenuEntity> children;


  /** menu: 菜单， button： 按钮 */
  private Integer type;
  /** 路径地址. */
  private String requestUrl;
  /** 请求方法(PUT/POST/DELETE). */
  private String requestMethod;
  /** 权限标示(用来控制界面的按钮显示) */
  private String keyword;
  /** 菜单角色 */
  private Set<SysRoleEntity> roles;
  /** 是否是外链 */
  private Boolean iframe;

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
  @Column(name = "title")
  public String getTitle() {
    return title;
  }

  public void setTitle(String title) {
    this.title = title;
  }

  @Basic
  @Column(name = "path")
  public String getPath() {
    return path;
  }

  public void setPath(String path) {
    this.path = path;
  }

  @Basic
  @Column(name = "component")
  public String getComponent() {
    return component;
  }

  public void setComponent(String component) {
    this.component = component;
  }

  @Basic
  @Column(name = "icon")
  public String getIcon() {
    return icon;
  }

  public void setIcon(String icon) {
    this.icon = icon;
  }

  @Basic
  @Column(name = "keep_alive")
  public Boolean getKeepAlive() {
    return keepAlive;
  }

  public void setKeepAlive(Boolean keepAlive) {
    this.keepAlive = keepAlive;
  }

  @Basic
  @Column(name = "hide_children_in_menu")
  public Boolean getHideChildrenInMenu() {
    return hideChildrenInMenu;
  }

  public void setHideChildrenInMenu(Boolean hideChildrenInMenu) {
    this.hideChildrenInMenu = hideChildrenInMenu;
  }

  @Basic
  @Column(name = "hidden")
  public Boolean getHidden() {
    return hidden;
  }

  public void setHidden(Boolean hidden) {
    this.hidden = hidden;
  }

  @Basic
  @Column(name = "hidden_header_content")
  public Boolean getHiddenHeaderContent() {
    return hiddenHeaderContent;
  }

  public void setHiddenHeaderContent(Boolean hiddenHeaderContent) {
    this.hiddenHeaderContent = hiddenHeaderContent;
  }

  @Basic
  @Column(name = "redirect")
  public String getRedirect() {
    return redirect;
  }

  public void setRedirect(String redirect) {
    this.redirect = redirect;
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
  @Column(name = "enabled")
  public Boolean getEnabled() {
    return enabled;
  }

  public void setEnabled(Boolean enabled) {
    this.enabled = enabled;
  }

  @Basic
  @Column(name = "type")
  public Integer getType() {
    return type;
  }

  public void setType(Integer type) {
    this.type = type;
  }

  @Basic
  @Column(name = "request_url")
  public String getRequestUrl() {
    return requestUrl;
  }

  public void setRequestUrl(String requestUrl) {
    this.requestUrl = requestUrl;
  }

  @Basic
  @Column(name = "request_method")
  public String getRequestMethod() {
    return requestMethod;
  }

  public void setRequestMethod(String requestMethod) {
    this.requestMethod = requestMethod;
  }

  @Basic
  @Column(name = "keyword")
  public String getKeyword() {
    return keyword;
  }

  public void setKeyword(String keyword) {
    this.keyword = keyword;
  }

  @JsonIgnore
  @ManyToMany(mappedBy = "menus")
  public Set<SysRoleEntity> getRoles() {
    return roles;
  }

  public void setRoles(Set<SysRoleEntity> roles) {
    this.roles = roles;
  }

  @Basic
  @Column(name = "iframe")
  public Boolean getIframe() {
    return iframe;
  }

  public void setIframe(Boolean iframe) {
    this.iframe = iframe;
  }

  @Basic
  @Column(name = "menu_sort")
  public Integer getMenuSort() {
    return menuSort;
  }

  public void setMenuSort(Integer menuSort) {
    this.menuSort = menuSort;
  }


  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @Transient
  public List<SysMenuEntity> getPermissions() {
    return permissions;
  }

  public void setPermissions(List<SysMenuEntity> permissions) {
    this.permissions = permissions;
  }

  @JsonInclude(JsonInclude.Include.NON_EMPTY)
  @Transient
  public List<SysMenuEntity> getChildren() {
    return children;
  }

  public void setChildren(List<SysMenuEntity> children) {
    this.children = children;
  }


  @Override
  public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;
    if (!super.equals(o)) return false;
    SysMenuEntity that = (SysMenuEntity) o;
    return Objects.equals(name, that.name) &&
      Objects.equals(title, that.title) &&
      Objects.equals(icon, that.icon) &&
      Objects.equals(path, that.path) &&
      Objects.equals(component, that.component) &&
      Objects.equals(keepAlive, that.keepAlive) &&
      Objects.equals(hideChildrenInMenu, that.hideChildrenInMenu) &&
      Objects.equals(hidden, that.hidden) &&
      Objects.equals(hiddenHeaderContent, that.hiddenHeaderContent) &&
      Objects.equals(redirect, that.redirect) &&
      Objects.equals(parentId, that.parentId) &&
      Objects.equals(enabled, that.enabled);
  }

  @Override
  public int hashCode() {
    return Objects.hash(super.hashCode(), name, title, icon, path, component, keepAlive, hideChildrenInMenu, hidden, hiddenHeaderContent, redirect, parentId, enabled);
  }
}
