package com.admin.auth.controller;

import com.admin.auth.dto.LoginDto;
import com.admin.auth.service.AuthService;
import com.admin.core.log.WebLog;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;

/**
 * 授权接口
 * @author fei
 * @date 2018/10/21
 */
@RestController
@RequestMapping("api/auth")
public class AuthController {
  private final AuthService authService;

  @Value("${app.jwt.header}")
  private String tokenHeader;

  @Autowired
  public AuthController(AuthService authService) {
    this.authService = authService;
  }

  /**
   * 用户登陆( 用户 密码 )
   * @param dto 用户表单
   * @return token
   * @throws AuthenticationException 授权异常
   */
  @WebLog(description = "用户登录。。。。。")
  @PostMapping("login")
  public String createAuthenticationToken(
    @RequestBody LoginDto dto) throws AuthenticationException {
    return authService.login(dto.getUsername(), dto.getPassword());
  }

  /**
   * 用户注销
   */
  @PutMapping("logout")
  public void logout() {
    authService.logout();
  }

  /**
   * 刷新 token 的有效时间
   * @param request 请求对象
   * @return 新 token
   * @throws AuthenticationException 授权异常
   */
  @GetMapping("refresh")
  public String refreshAndGetAuthenticationToken(
    HttpServletRequest request) throws AuthenticationException{
    String token = request.getHeader(tokenHeader);

    return authService.refresh(token);
  }

}

