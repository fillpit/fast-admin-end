package com.kenfei.admin.user.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.user.entity.SysMenuEntity;
import com.kenfei.admin.user.enums.MenuTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 菜单表单
 *
 * @author fei
 * @date 2018/7/30
 */
@Data
public class SysMenuDto implements DtoConvert<SysMenuEntity> {
  /** 菜单名称(路由名称). */
  @NotBlank private String name;
  /** 菜单名称. */
  @NotBlank private String title;
  /** 图标 */
  private String icon;
  /** uri 路径. */
  @NotBlank private String path;
  /** 组件名. */
  @NotBlank private String component;
  /** 是否需要缓存. */
  @NotNull private Boolean keepAlive;
  /** 用于隐藏不需要在菜单中展示的子路由。用法可以查看 个人设置 的配置。 */
  private Boolean hideChildrenInMenu;
  /** 可以在菜单中不展示这个路由，包括子路由。效果可以查看 other 下的路由配置。 */
  @NotNull private Boolean hidden;
  /** 可以强制当前页面不显示 PageHeader 组件中的页面带的 面包屑和页面标题栏 */
  private Boolean hiddenHeaderContent;
  /** 菜单重定向（目录菜单可用） */
  private String redirect;
  /** 上级菜单ID. */
  private Long parentId;
  /** 是否启用. */
  @NotNull private Boolean enabled;
  /** 是否是外链 */
  private Boolean iframe;
  /** 0：目录， 1: 菜单， 2： 按钮 */
  private String type;

  /** 菜单排序 */
  private Integer menuSort;

  @Override
  public SysMenuEntity convert(Class<SysMenuEntity> sysMenuEntityClass) {
    SysMenuEntity entity = DtoConvert.super.convert(sysMenuEntityClass);
    entity.setType(MenuTypeEnum.MENU.value());

    return entity;
  }
}
