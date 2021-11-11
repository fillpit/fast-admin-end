package com.kenfei.admin.auth.service;

import com.kenfei.admin.auth.config.security.JwtUser;
import com.kenfei.admin.auth.enums.LoginStatusEnum;
import com.kenfei.admin.auth.model.CurrentUser;
import com.kenfei.admin.auth.service.vo.OnlineUserVo;
import com.kenfei.admin.auth.util.RedisUtils;
import com.kenfei.admin.core.utils.*;
import com.kenfei.admin.user.service.SysLoginInfoService;
import com.kenfei.admin.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Pageable;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.validation.annotation.Validated;

import javax.servlet.http.HttpServletRequest;
import java.util.*;

/**
 * 在线用户功能
 * @author fei
 * @since 2019/12/25 14:38
 */
@Slf4j
@Validated
@Service
public class OnlineUserService {
  @Autowired private SysUserService sysUserService;
  @Autowired private RedisUtils redisUtils;
  @Autowired private SysLoginInfoService sysLoginInfoService;

  @Value("${app.jwt.expiration}")
  private Long expiration;

  /**
   * 保存在线用户信息
   * @param jwtUser 用户信息
   * @param token key
   * @param request /
   */
  @Async
  public void save(JwtUser jwtUser, String token, HttpServletRequest request) {
    String dept = Optional.ofNullable(jwtUser.getDept()).map(JwtUser.JwtDept::getName).orElse("");
    String ip = IpUtils.getIpAddr(request);
    String browser = ServletUtils.getBrowser(request);
    String address = AddressUtils.getRealAddressByIp(ip);

    OnlineUserVo onlineUserVo =
      null;
    try {
      onlineUserVo = OnlineUserVo.builder()
        .userName(jwtUser.getUsername())
        .nickName(jwtUser.getNickName())
        .dept(dept)
        .browser(browser)
        .ip(ip)
        .address(address)
        .key(EncryptUtils.desEncrypt(token))
        .loginTime(new Date())
        .build();
    } catch (Exception e) {
      log.error(e.getMessage(),e);
    }

    // 记录登陆日志(异步)
    sysLoginInfoService.recordLoginInfo(
      request, jwtUser.getUsername(), LoginStatusEnum.LOGIN_SUCCESS, "登陆成功");

    redisUtils.set(getKey(token), onlineUserVo, expiration);
  }

  /**
   * 查询全部数据
   * @param filter /
   * @param pageable /
   * @return /
   */
  public Map<String,Object> getAll(String filter, Pageable pageable){
    List<OnlineUserVo> onlineUserDtos = getAll(filter);
    return PageUtil.toPage(
      PageUtil.toPage(pageable.getPageNumber(),pageable.getPageSize(), onlineUserDtos),
      onlineUserDtos.size()
    );
  }

  /**
   * 查询全部数据，不分页
   * @param filter /
   * @return /
   */
  public List<OnlineUserVo> getAll(String filter){
    List<String> keys = redisUtils.scan(getKey("*"));
    Collections.reverse(keys);
    List<OnlineUserVo> onlineUserDtos = new ArrayList<>();
    for (String key : keys) {
      OnlineUserVo onlineUserDto = (OnlineUserVo) redisUtils.get(key);
      if(StringUtils.hasLength(filter)){
        if(onlineUserDto.toString().contains(filter)){
          onlineUserDtos.add(onlineUserDto);
        }
      } else {
        onlineUserDtos.add(onlineUserDto);
      }
    }
    onlineUserDtos.sort((o1, o2) -> o2.getLoginTime().compareTo(o1.getLoginTime()));
    return onlineUserDtos;
  }

  /**
   * 下线
   * @param token /
   */
  public void logout(String token) {
    JwtUser jwtUser = CurrentUser.user();

    // 记录注销日志(异步)
    sysLoginInfoService.recordLoginInfo(
      ServletUtils.getRequest(), jwtUser.getUsername(), LoginStatusEnum.LOGOUT, "注销");

    redisUtils.del(getKey(token));
  }

  /**
   * 强制下线
   * @param token token
   */
  public void kickOut(String token) {
    JwtUser jwtUser = CurrentUser.user();

    // 记录强制下线日志(异步)
    sysLoginInfoService.recordLoginInfo(
      ServletUtils.getRequest(), jwtUser.getUsername(), LoginStatusEnum.KILL_OUT, "强制下线");

    redisUtils.del(getKey(token));
  }


  /**
   * 查询用户
   * @param token /
   * @return /
   */
  public OnlineUserVo getOne(String token) {
    Object ob = redisUtils.get(getKey(token));
    return Objects.isNull(ob) ? null : (OnlineUserVo) ob;
  }

  private String getKey(String key) {
    return "online_token_".concat(key);
  }
}
