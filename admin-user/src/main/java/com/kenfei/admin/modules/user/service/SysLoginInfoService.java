package com.kenfei.admin.modules.user.service;

import com.kenfei.admin.modules.auth.enums.LoginStatusEnum;
import com.kenfei.admin.core.common.base.InterfaceService;
import com.kenfei.admin.modules.user.entity.SysLoginInfoEntity;
import org.springframework.scheduling.annotation.Async;

import javax.servlet.http.HttpServletRequest;

/**
 * 登陆日志(SysLoginInfo)表服务接口
 *
 * @author fei
 * @since 2020-10-22 15:25:20
 */
public interface SysLoginInfoService extends InterfaceService<SysLoginInfoEntity, Long> {

  /**
   * 保存登陆日志
   * @param username 登陆用户名
   * @param status 状态
   * @param message 信息
   * @param args 参数
   * @return 登陆日志对象
   */
  @Async
  SysLoginInfoEntity recordLoginInfo(final HttpServletRequest request, final String username, final LoginStatusEnum status, final String message, final Object... args);
}
