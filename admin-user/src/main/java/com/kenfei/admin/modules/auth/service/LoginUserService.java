package com.kenfei.admin.modules.auth.service;

import com.kenfei.admin.core.common.UploadProperties;
import com.kenfei.admin.modules.auth.config.security.JwtUser;
import com.kenfei.admin.modules.auth.model.CurrentUser;
import com.kenfei.admin.core.common.exception.AppException;
import com.kenfei.admin.core.utils.FileUtils;
import com.kenfei.admin.modules.quartz.utils.StringUtils;
import com.kenfei.admin.modules.user.entity.SysMenuEntity;
import com.kenfei.admin.modules.user.entity.SysUserEntity;
import com.kenfei.admin.modules.user.service.SysMenuService;
import com.kenfei.admin.modules.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;
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

  private static final String FOLDER = "avatar/";

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
    return sysUserService.findById(CurrentUser.userId());
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
    SysUserEntity user = sysUserService.findById(CurrentUser.userId());
    String oldPath = user.getAvatar();
    String fileName = FileUtils.upload(multipartFile, uploadProperties.getSavePath(FOLDER));
    Assert.notNull(fileName, "文件保存失败");

    String requestPath = uploadProperties.getRequestPath(FOLDER) + fileName;
    user.setAvatar(requestPath);
    sysUserService.update(user);
    if (StringUtils.hasLength(oldPath)) {
      FileUtils.del(uploadProperties.getSavePath(FOLDER) + oldPath);
    }

    return requestPath;
  }

  public void updateCenter(SysUserEntity entity) {
    sysUserService.update(entity);
  }

}

