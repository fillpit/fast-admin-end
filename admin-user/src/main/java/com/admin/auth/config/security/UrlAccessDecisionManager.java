package com.admin.auth.config.security;

import org.springframework.security.access.AccessDecisionManager;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.access.ConfigAttribute;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.InsufficientAuthenticationException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

import java.util.Collection;

/**
 * 判断当前登陆用户是否有访问当前资源的角色
 * @author fei
 * @since 2018/10/21
 */
@Component
public class UrlAccessDecisionManager implements AccessDecisionManager {
  @Override
  public void decide(Authentication authentication, Object o, Collection<ConfigAttribute> collection) throws AccessDeniedException, InsufficientAuthenticationException {
    for (ConfigAttribute ca : collection) {
      //当前请求需要的权限
      String needRole = ca.getAttribute();
      if ("ROLE_LOGIN".equals(needRole)) {
        // 如果是匿名登陆者跳到登陆界面
        if (authentication instanceof AnonymousAuthenticationToken) {
          throw new BadCredentialsException("未登录");
        } else {
          return;
        }
      }

      // 当前用户所具有的权限
      Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
      for (GrantedAuthority authority : authorities) {
        if (authority.getAuthority().equals(needRole)) {
          return;
        }
      }
    }
    throw new AccessDeniedException("权限不足!");
  }

  @Override
  public boolean supports(ConfigAttribute configAttribute) {
    return true;
  }

  @Override
  public boolean supports(Class<?> aClass) {
    return true;
  }
}
