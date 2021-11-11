package com.kenfei.admin.auth.service.vo;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.util.Date;

/**
 * 在线用户信息
 * @author fei
 * @date 2021/8/27 11:46
 */
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class OnlineUserVo implements Serializable {
  /** 用户名 */
  private String userName;
  /** 昵称 */
  private String nickName;
  /** 岗位 */
  private String dept;
  /** 浏览器 */
  private String browser;
  /** IP */
  private String ip;
  /** 地址 */
  private String address;
  /** token */
  private String key;
  /** 登录时间 */
  private Date loginTime;
}
