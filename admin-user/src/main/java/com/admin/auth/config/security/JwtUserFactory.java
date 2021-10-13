package com.admin.auth.config.security;

import com.admin.user.entity.SysMenuEntity;
import com.admin.user.entity.SysRoleEntity;
import com.admin.user.entity.SysUserEntity;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * 鉴权用户构造工厂.
 * @author fei
 * @date 2018/10/22
 */
public class JwtUserFactory {

  /**
   * 构建jwt 用户信息.
   * @param user 系统用户
   * @return jwt 用户对象
   */
  public static JwtUser create(SysUserEntity user) {
    return new JwtUser(user);
  }

  private static List<GrantedAuthority> mapToGrantedAuthorities(List<String> authorities) {
    return authorities.stream()
      .map(SimpleGrantedAuthority::new)
      .collect(Collectors.toList());
  }
}
