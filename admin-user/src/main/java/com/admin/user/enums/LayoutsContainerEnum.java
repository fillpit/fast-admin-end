package com.admin.user.enums;

import com.admin.core.enums.BaseEnum;

/**
 * 前端布局容器.
 *
 * @author fei
 * @since 2019-04-01 14:13
 */
public enum LayoutsContainerEnum implements BaseEnum<String> {
  /** 空. */
  BLANK("layouts/BlankLayout"),
  /** 基本. */
  ROUTE("layouts/RouteView"),
  /** 卡片. */
  PAGE_VIEW("layouts/PageView");

  private String value;

  LayoutsContainerEnum(String value) {
    this.value = value;
  }

  @Override
  public String value() {
    return this.value;
  }

}
