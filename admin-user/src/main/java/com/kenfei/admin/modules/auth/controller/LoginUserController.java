package com.kenfei.admin.modules.auth.controller;

import com.kenfei.admin.modules.auth.config.security.JwtUser;
import com.kenfei.admin.modules.auth.dto.UpdateUserInfoDto;
import com.kenfei.admin.modules.auth.model.CurrentUser;
import com.kenfei.admin.modules.auth.service.LoginUserService;
import com.kenfei.admin.core.common.exception.AppException;
import com.kenfei.admin.core.config.resolvers.JsonParam;
import com.kenfei.admin.modules.user.dto.SysUserDto;
import com.kenfei.admin.modules.user.entity.SysMenuEntity;
import com.kenfei.admin.modules.user.entity.SysUserEntity;
import com.kenfei.admin.modules.user.service.SysUserService;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * 获取登录用户的一些信息
 * @author fei
 * @date 2021/7/30 13:01
 */
@RestController
@RequestMapping("api/user")
public class LoginUserController {
  @Autowired private LoginUserService loginUserService;
  @Autowired private SysUserService sysUserService;

  /**
   * 更新用户信息 (用户自己修改)
   * @param dto 表单数据
   * @return 更新后的用户对象
   */
  @PutMapping("me")
  public SysUserEntity updateByMe(@RequestBody SysUserDto dto) {
    Long id = CurrentUser.userId();
    SysUserEntity sysUser = sysUserService.findById(id);
    BeanUtils.copyProperties(dto, sysUser, "enabled");

    return sysUserService.update(sysUser);
  }

  /**
   * 修改密码
   * @param oldPwd 旧密码
   * @param newPwd 新密码
   * @return 当前用户对象
   */
  @PutMapping("modify")
  public SysUserEntity modify(
    @JsonParam @NotEmpty String oldPwd,
    @JsonParam @NotEmpty @Size(min = 6, max = 18)
      String newPwd) {
    Long id = CurrentUser.userId();
    SysUserEntity sysUser = sysUserService.findById(id);

    // 验证旧密码
    BCryptPasswordEncoder encoder = new BCryptPasswordEncoder();
    if (!encoder.matches(oldPwd, sysUser.getPassword())) {
      throw new AppException("原密码不正确");
    }
    String encode = encoder.encode(newPwd);
    sysUser.setPassword(encode);
    return sysUserService.update(sysUser);
  }

  /**
   * 上传头像
   * @param avatar 文件
   * @return /
   */
  @PostMapping(value = "/updateAvatar")
  public String updateAvatar(@RequestParam MultipartFile avatar){
    return loginUserService.uploadAvatar(avatar);
  }


  /**
   * 获取用户信息
   * @return
   */
  @GetMapping(value = "/user-info")
  public JwtUser getUserInfo() {
    return CurrentUser.user();
  }

  /**
   * 获取用户菜单
   * @return 菜单列表
   */
  @GetMapping(value = "user-menu")
  public List<SysMenuEntity> findUserMenu() {
    return loginUserService.findUserMenu();
  }

  /**
   * 修改个人资料（自己修改自己的）
   * @param dto /
   * @return /
   */
  @PutMapping(value = "center")
  public void center(@Valid @RequestBody UpdateUserInfoDto dto){
    SysUserEntity sysUser = sysUserService.findById(CurrentUser.userId());
    dto.pathProperties(sysUser);

    loginUserService.updateCenter(sysUser);
  }

}
