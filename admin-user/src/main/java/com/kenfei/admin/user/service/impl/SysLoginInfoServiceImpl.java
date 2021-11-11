package com.kenfei.admin.user.service.impl;

import com.kenfei.admin.auth.enums.LoginStatusEnum;
import com.kenfei.admin.core.common.base.AbstractServiceImpl;
import com.kenfei.admin.core.utils.AddressUtils;
import com.kenfei.admin.core.utils.IpUtils;
import com.kenfei.admin.auth.util.LogUtils;
import com.kenfei.admin.core.utils.ServletUtils;
import com.kenfei.admin.user.entity.SysLoginInfoEntity;
import com.kenfei.admin.user.repository.SysLoginInfoRepository;
import com.kenfei.admin.user.service.SysLoginInfoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

/**
 * 登陆日志(SysLoginInfo)表服务实现类
 *
 * @author fei
 * @since 2020-10-22 15:25:20
 */
@Service
@Validated
public class SysLoginInfoServiceImpl extends AbstractServiceImpl<SysLoginInfoEntity, Long>
    implements SysLoginInfoService {

  private static final Logger sys_user_logger = LoggerFactory.getLogger("sys-user");
  private final SysLoginInfoRepository sysLoginInfoRepository;

  @Autowired
  public SysLoginInfoServiceImpl(SysLoginInfoRepository sysLoginInfoRepository) {
    super(sysLoginInfoRepository);
    this.sysLoginInfoRepository = sysLoginInfoRepository;
  }

  @Override
  public SysLoginInfoEntity recordLoginInfo(HttpServletRequest request,
                                            String username, LoginStatusEnum status, String message, Object... args) {
    String ip = IpUtils.getIpAddr(request);
    String address = AddressUtils.getRealAddressByIp(ip);
    StringBuilder s = new StringBuilder();
    s.append(LogUtils.getBlock(ip));
    s.append(address);
    s.append(LogUtils.getBlock(username));
    s.append(LogUtils.getBlock(status));
    s.append(LogUtils.getBlock(message));
    // 打印信息到日志
    sys_user_logger.info(s.toString(), args);
    // 获取客户端操作系统
    String os = ServletUtils.getClientOS(request);
    // 获取客户端浏览器
    String browser = ServletUtils.getBrowser(request);
    // 封装对象
    SysLoginInfoEntity logininfor = new SysLoginInfoEntity();
    logininfor.setLoginName(username);
    logininfor.setIpaddr(ip);
    logininfor.setLoginLocation(address);
    logininfor.setBrowser(browser);
    logininfor.setOs(os);
    logininfor.setMsg(message);
    logininfor.setLoginTime(new Date());

    List<LoginStatusEnum> arr = Arrays.asList(LoginStatusEnum.LOGIN_SUCCESS, LoginStatusEnum.LOGOUT, LoginStatusEnum.REGISTER);
    // 日志状态
    if (arr.contains(status)) {
      logininfor.setStatus(true);
    } else if (LoginStatusEnum.LOGIN_FAIL.equals(status)) {
      logininfor.setStatus(false);
    }
    // 插入数据
    return this.save(logininfor);
  }

}
