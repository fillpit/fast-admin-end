package com.admin.core.enums;

import com.fasterxml.jackson.annotation.JsonValue;

/**
 * 枚举基类,所有的枚举都应该实现这个类
 *
 * @author fei
 * @date 2018/6/14
 */
public interface BaseEnum<V> {
  /**
   * 枚举值的key
   *
   * @return 对应类型的key
   */
  @JsonValue
  V value();
}
