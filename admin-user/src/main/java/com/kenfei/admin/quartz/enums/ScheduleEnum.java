package com.kenfei.admin.quartz.enums;

import com.kenfei.admin.core.common.base.BaseEnum;
import com.kenfei.admin.core.common.exception.AppException;

/**
 * @author fei
 * @since 2020/10/29 11:49 上午
 */
public enum ScheduleEnum implements BaseEnum<Integer> {
  /** 默认 */
  MISFIRE_DEFAULT(0),
  /** 立即触发执行 */
  MISFIRE_IGNORE_MISFIRES(1),
  /** 触发一次执行 */
  MISFIRE_FIRE_AND_PROCEED(2),
  /** 不触发立即执行 */
  MISFIRE_DO_NOTHING(3),
  ;
  private Integer val;

  ScheduleEnum(Integer val) {
    this.val = val;
  }

  public Integer getVal() {
    return val;
  }

  public static ScheduleEnum get(Integer val) {
    for(ScheduleEnum item : values()){
      if (item.val.equals(val)) {
        return item;
      }
    }
    throw new AppException("未定义的值：" + val);
  }

  @Override
  public Integer value() {
    return val;
  }

  @Override
  public String toString() {
    return String.valueOf(val);
  }
}
