package com.kenfei.admin.user.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.user.entity.SysJobsEntity;
import com.kenfei.admin.user.entity.SysRoleEntity;
import com.kenfei.admin.user.entity.SysUserEntity;
import lombok.Data;

import javax.validation.constraints.*;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 用户注册时接收参数.
 *
 * @author fei
 * @date 2017/9/30
 */
@Data
public class SysUserDto implements DtoConvert<SysUserEntity> {
  private static final String DEFAULT_PASSWORD = "123456";

  /** 用户名. */
  @NotBlank(message = "用户名不能为空")
  private String userName;
  /** 昵称 */
  private String nickName;
  /** 手机号码. */
  @NotBlank
  @Pattern(regexp="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message="手机号格式不正确")
  private String phone;
  /** email. */
  private String email;
  /** 头像. */
  private String avatar;
  /** 性别 */
  private String gender;
  /** 用户状态. */
  @NotNull private Boolean enabled;
  /** 部门 */
  private Long deptId;
  /** 岗位 */
  @NotEmpty
  private List<Long> jobs;
  /** 角色 */
  @NotEmpty
  private List<Long> roles;

  @Override
  public SysUserEntity convert(Class<SysUserEntity> sysUserEntityClass) {
    SysUserEntity entity = DtoConvert.super.convert(sysUserEntityClass);
    // 岗位
    entity.setJobs(this.jobs.stream().map(SysJobsEntity::new).collect(Collectors.toSet()));
    // 角色
    entity.setRoles(this.roles.stream().map(SysRoleEntity::new).collect(Collectors.toSet()));
    // 默认密码
    entity.setPassword(DEFAULT_PASSWORD);
    return entity;
  }
}
