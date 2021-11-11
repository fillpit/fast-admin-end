package com.kenfei.admin.user.enums;

import com.kenfei.admin.core.common.base.BaseEnum;


/**
 * 权限类型
 * @author fei
 * @date 2021/7/29 13:44
 */
public enum MenuTypeEnum implements BaseEnum<Integer> {
  /** 菜单. */
  MENU(1),
  /** 按钮. */
  BUTTON(2),
  ;

  private Integer value;

  MenuTypeEnum(Integer value) {
    this.value = value;
  }

  @Override
  public Integer value() {
    return this.value;
  }

}
