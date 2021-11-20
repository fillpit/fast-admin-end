package com.kenfei.admin.modules.auth.service;

import com.kenfei.admin.modules.auth.config.security.JwtUser;
import com.kenfei.admin.modules.auth.config.security.JwtUserDetailsServiceImpl;
import com.kenfei.admin.modules.auth.service.vo.OnlineUserVo;
import com.kenfei.admin.modules.auth.util.JwtTokenUtil;
import com.kenfei.admin.core.utils.ServletUtils;
import com.kenfei.admin.modules.user.entity.SysUserEntity;
import com.kenfei.admin.modules.user.service.SysUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * 授权业务
 *
 * @author fei
 * @date 2018/10/25
 */
@Slf4j
@Service
@Validated
public class AuthService {
  private final AuthenticationManager authenticationManager;
  private final JwtUserDetailsServiceImpl jwtUserDetailsService;
  private final JwtTokenUtil jwtTokenUtil;
  private final SysUserService userService;
  private final OnlineUserService onlineUserService;

  @Autowired
  public AuthService(
      AuthenticationManager authenticationManager,
      JwtUserDetailsServiceImpl jwtUserDetailsService,
      JwtTokenUtil jwtTokenUtil,
      SysUserService userService,
      OnlineUserService onlineUserService) {
    this.authenticationManager = authenticationManager;
    this.jwtUserDetailsService = jwtUserDetailsService;
    this.jwtTokenUtil = jwtTokenUtil;
    this.userService = userService;
    this.onlineUserService = onlineUserService;
  }

  /**
   * 登陆
   *
   * @param userName 用户名
   * @param password 密码
   * @return token
   */
  @Transactional(rollbackFor = Exception.class)
  public String login(@NotBlank String userName, @NotBlank String password) {
    UsernamePasswordAuthenticationToken upToken =
        new UsernamePasswordAuthenticationToken(userName, password);
    final Authentication authentication = authenticationManager.authenticate(upToken);
    SecurityContextHolder.getContext().setAuthentication(authentication);

    final JwtUser jwtUser = (JwtUser) authentication.getPrincipal();
    return this.authLogin(jwtUser);
  }

  private String authLogin(JwtUser jwtUser) {
    // 更新用户的最后登陆时间
    updateLastLoginTime(jwtUser.getId());

    // 生成用户token
    String token = jwtTokenUtil.generateToken(jwtUser);

    // 保存在线信息
    onlineUserService.save(jwtUser, token, ServletUtils.getRequest());

    return token;
  }

  /**
   * 刷新 token
   *
   * @param oldToken 旧token
   * @return 新 token
   */
  public String refresh(String oldToken) {
    String token = jwtTokenUtil.subTokenPrefix(oldToken);
    String username = jwtTokenUtil.getUsernameFromToken(token);
    JwtUser jwtUser = (JwtUser) jwtUserDetailsService.loadUserByUsername(username);
    if (jwtTokenUtil.canTokenBeRefreshed(token, jwtUser.getLastLoginTime())) {
      String newToken = jwtTokenUtil.refreshToken(token);

      // 刷新 redis token 的有效时间和value
      onlineUserService.save(jwtUser, newToken, ServletUtils.getRequest());
      //删除旧 token
      onlineUserService.logout(token);

      return newToken;
    }
    return null;
  }

  /**
   * 注销当前用户
   * @return true: 成功, false: 失败
   */
  public void logout() {
    String token = jwtTokenUtil.getLoginToken(ServletUtils.getRequest());

    // 删除当前用户在redis里的key
    onlineUserService.logout(token);
  }

  /**
   * 判断 token 是否有效
   *
   * @param token token
   * @return true: 木有， false: 已注销
   */
  public Boolean isLogout(String token) {
    OnlineUserVo vo = onlineUserService.getOne(token);
    return null != vo;
  }


  /**
   * 更新用户的最后登陆日期
   * @param id /
   */
  private void updateLastLoginTime(Long id){
    SysUserEntity sysUser = userService.findById(id);
    sysUser.setLastLoginTime(new Date());
    userService.update(sysUser);
  }

}
