package com.admin.user.dto;

import java.util.Date;
import com.admin.core.basic.DtoConvert;
import com.admin.user.entity.SysDeptEntity;
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
