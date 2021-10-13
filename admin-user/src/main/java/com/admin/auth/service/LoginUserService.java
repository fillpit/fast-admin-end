package com.admin.auth.service;

import com.admin.auth.config.security.JwtUser;
import com.admin.auth.model.CurrentUser;
import com.admin.core.exception.AppException;
import com.admin.core.config.properties.UploadProperties;
import com.admin.core.utils.FileUtils;
import com.admin.quartz.utils.StringUtils;
import com.admin.user.entity.SysMenuEntity;
import com.admin.user.entity.SysUserEntity;
import com.admin.user.service.SysMenuService;
import com.admin.user.service.SysUserService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.*;

/**
 * @author fei
 * @date 2021/7/30 13:08
 */
@Service
public class LoginUserService {

  private final SysMenuService sysMenuService;
  private final SysUserService sysUserService;
  private final UploadProperties uploadProperties;

  public LoginUserService(SysMenuService sysMenuService, SysUserService sysUserService, UploadProperties uploadProperties) {
    this.sysMenuService = sysMenuService;
    this.sysUserService = sysUserService;
    this.uploadProperties = uploadProperties;
  }

  /**
   * 当前登录用户
   * @return /
   */
  public SysUserEntity loginUser() {
    return sysUserService.findById(CurrentUser.currentUserId());
  }

  /**
   * 获取用户菜单
   * @return 用户菜单列表
   */
  public List<SysMenuEntity> findUserMenu() {
    Set<JwtUser.JwtRole> roles = CurrentUser.currentUserRole();

    List<SysMenuEntity> menus = new LinkedList<>();
    for (JwtUser.JwtRole role : roles) {
      menus.addAll(sysMenuService.findByRoleIds(new Long[]{role.getRoleId()}));
    }

    return sysMenuService.buildTree(menus);
  }

  /**
   * 上传头像
   * @param multipartFile 文件
   * @return /
   */
  public String uploadAvatar(MultipartFile multipartFile) {
    // 验证文件上传的格式
    String image = "gif jpg png jpeg";
    String fileType = FileUtils.getExtensionName(multipartFile.getOriginalFilename());
    if(fileType != null && !image.contains(fileType)){
      throw new AppException("文件格式错误！, 仅支持 " + image +" 格式");
    }
    SysUserEntity user = sysUserService.findById(CurrentUser.currentUserId());
    String oldPath = user.getAvatar();
    String fileName = FileUtils.upload(multipartFile, uploadProperties.getSavePath());
    user.setAvatar(Objects.requireNonNull(fileName));
    sysUserService.update(user);
    if (StringUtils.hasLength(oldPath)) {
      FileUtils.del(uploadProperties.getSavePath() + oldPath);
    }

    return "upload/" + fileName;
  }

  public void updateCenter(SysUserEntity entity) {
    sysUserService.update(entity);
  }
}
