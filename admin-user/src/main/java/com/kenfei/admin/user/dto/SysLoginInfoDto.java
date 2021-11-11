package com.kenfei.admin.user.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.user.entity.SysLoginInfoEntity;
import lombok.Data;

import java.util.Date;

/**
 * 登陆日志(SysLoginInfo)表单数据
 *
 * @author fei
 * @since 2020-10-22 15:25:20
 */
@Data
public class SysLoginInfoDto implements DtoConvert<SysLoginInfoEntity> {

  private Long id;
  /** 登陆用户名 */
  private String loginName;
  /** 0=成功,1=失败 */
  private Boolean status;
  /** 登录地址 */
  private String ipaddr;
  /** 登录地点 */
  private Integer loginLocation;
  /** 浏览器 */
  private String browser;
  /** 操作系统 */
  private String os;
  /** 提示消息 */
  private String msg;
  /** 访问时间 */
  private Date loginTime;
}
