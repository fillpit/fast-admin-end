package com.kenfei.admin.modules.user.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.modules.user.entity.SysRoleEntity;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * 角色表单.
 * @author fei
 * @date 2017/10/17
 */
@Data
public class SysRoleDto implements DtoConvert<SysRoleEntity> {
  @NotNull private String name;
  private Boolean enabled;
  /** 权限级别，低级别的无法编辑高级别的 */
  @Min(1) private Integer level;
  /** 数据范围 */
  private String dataScope;
  /** 描述. */
  private String description;
}
