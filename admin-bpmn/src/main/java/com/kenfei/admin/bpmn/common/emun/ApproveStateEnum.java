package com.kenfei.admin.bpmn.common.emun;

import com.kenfei.admin.core.common.base.BaseEnum;

/**
 * @author fei
 * @date 2021/11/4 10:17
 */
public enum ApproveStateEnum implements BaseEnum<Integer> {
  /** 开始 */
  START,
  /** 结束 */
  END,
  /** 同意 */
  COMPLETE,
  /** 后加签 */
  after_addsign,
  /** 委派 */
  delegate,
  /** 加签 */
  before_addsign,
  /** 暂存 */
  claim,
  /** 协同 */
  coordination,
  /** 评审 */
  review,
  /** 转办 */
  turn_do,
  /** 审批 */
  approve,
  /** 转阅 */
  turn_read,
  /** 驳回 */
  reject,
  /** 撤回 */
  revoke,
  ;

  @Override
  public Integer value() {
    return this.ordinal();
  }
}
