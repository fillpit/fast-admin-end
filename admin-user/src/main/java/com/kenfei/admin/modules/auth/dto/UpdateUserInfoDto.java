package com.kenfei.admin.modules.auth.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.modules.user.entity.SysUserEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;

/**
 * 更新用户资料（更新自己的）
 * @author fei
 * @date 2021/8/24 17:01
 */
@Data
public class UpdateUserInfoDto implements DtoConvert<SysUserEntity> {
  /** 昵称 */
  private String nickName;
  /** 手机号码. */
  @NotBlank
  @Pattern(regexp="^((13[0-9])|(15[^4,\\D])|(18[0,5-9]))\\d{8}$", message="手机号格式不正确")
  private String phone;
  /** email. */
  @NotBlank private String email;
  /** 性别 */
  private String gender;
}
