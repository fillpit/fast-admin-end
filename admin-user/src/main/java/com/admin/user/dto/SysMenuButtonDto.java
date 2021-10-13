package com.admin.user.dto;

import com.admin.core.basic.DtoConvert;
import com.admin.user.entity.SysMenuEntity;
import com.admin.user.enums.MenuTypeEnum;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 菜单按钮表单
 * @author fei
 * @date 2021/8/15 23:08
 */
@Data
public class SysMenuButtonDto implements DtoConvert<SysMenuEntity> {
  /** 菜单名称(路由名称). */
  @NotBlank
  private String name;
  /** 权限标示(用来控制界面的按钮显示) */
  private String keyword;
  /** 路径地址. */
  @NotNull private String requestUrl;
  /** 请求方法(PUT/POST/DELETE). */
  @NotNull private String requestMethod;
  /** 上级菜单ID. */
  @NotNull private Long parentId;
  /** 是否启用. */
  @NotNull
  private Boolean enabled;

  @Override
  public SysMenuEntity convert(Class<SysMenuEntity> sysMenuEntityClass) {
    SysMenuEntity entity = DtoConvert.super.convert(sysMenuEntityClass);
    entity.setType(MenuTypeEnum.BUTTON.value());

    return entity;
  }
}
