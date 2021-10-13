package com.admin.core.enums;

import com.fasterxml.jackson.annotation.JsonCreator;

/**
 * 有效标示枚举类
 *
 * @author fei
 * @date 2018/7/21
 */
public enum EnabledEnum implements BaseEnum<Integer> {
  /** 禁用状态 */
  OFF(0, "禁用"),
  /** 启用状态 */
  ON(1, "开启");

  /** 值 */
  private Integer value;
  /** 解释 */
  private String note;

  EnabledEnum(Integer value, String note) {
    this.value = value;
    this.note = note;
  }

  @Override
  public Integer value() {
    return value;
  }

  public String note() {
    return note;
  }

  @JsonCreator
  public static EnabledEnum valueOf(int value) {
    for (EnabledEnum i : values()) {
      if (i.value().equals(value)) {
        return i;
      }
    }
    throw new IllegalArgumentException("没有这个枚举类型");
  }
}
