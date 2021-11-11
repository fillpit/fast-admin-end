package com.kenfei.admin.user.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.user.entity.SysDeptEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * (SysDept)表单类
 *
 * @author kenfei
 * @since 2021-08-16 16:23:36
 */
@Data
public class SysDeptDto implements DtoConvert<SysDeptEntity> {

  @NotNull private Integer deptSort;

  @NotNull private Boolean enabled;

  @NotBlank private String name;

  /** 上级部门 */
  private Long parentId;
}
