package com.admin.auth.config.security;

import com.admin.user.entity.SysMenuEntity;
import com.admin.user.entity.SysRoleEntity;
import com.admin.user.entity.SysUserEntity;
import com.admin.user.repository.SysUserRepository;
import com.admin.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * 用户名查询业务(security 用)
 *
 * @author fei
 * @date 2018/10/21
 */
@Service
public class JwtUserDetailsServiceImpl implements UserDetailsService {
  private final SysUserService sysUserService;

  @Autowired
  public JwtUserDetailsServiceImpl(SysUserService sysUserService) {
    this.sysUserService = sysUserService;
  }

  @Override
  public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
    SysUserEntity sysUser = sysUserService.findByUserName(s);
    Assert.notNull(sysUser, "用户名错误");

    return JwtUserFactory.create(sysUser);
  }

}
