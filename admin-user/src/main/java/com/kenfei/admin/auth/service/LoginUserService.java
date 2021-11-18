package com.kenfei.admin.auth.service;

import com.kenfei.admin.auth.config.security.JwtUser;
import com.kenfei.admin.auth.model.CurrentUser;
import com.kenfei.admin.core.common.exception.AppException;
import com.kenfei.admin.core.utils.FileUtils;
import com.kenfei.admin.quartz.utils.StringUtils;
import com.kenfei.admin.user.entity.SysMenuEntity;
import com.kenfei.admin.user.entity.SysUserEntity;
import com.kenfei.admin.user.service.SysMenuService;
import com.kenfei.admin.user.service.SysUserService;
import org.springframework.beans.factory.annotation.Value;
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

  @Value("${spring.servlet.multipart.location}")
  private String filePath;

  @Value("${app.location.source-url}")
  private String localSourceMapping;

  private static final String FOLDER = "avatar/";

  public LoginUserService(SysMenuService sysMenuService, SysUserService sysUserService) {
    this.sysMenuService = sysMenuService;
    this.sysUserService = sysUserService;
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
    String fileName = FileUtils.upload(multipartFile, getAvatarSavePath());
    user.setAvatar(Objects.requireNonNull(fileName));
    sysUserService.update(user);
    if (StringUtils.hasLength(oldPath)) {
      FileUtils.del(getAvatarSavePath() + oldPath);
    }

    return getAvatarRequestPath() + fileName;
  }

  public void updateCenter(SysUserEntity entity) {
    sysUserService.update(entity);
  }

  /**
   * 获取头像访问路径
   * @return /
   */
  private String getAvatarRequestPath() {
    return localSourceMapping + FOLDER;
  }

  /**
   * 获取头像保存的路径
   * @return
   */
  private String getAvatarSavePath() {
    return filePath + FOLDER;
  }
}

