package com.kenfei.admin.modules.user.dto;

import com.kenfei.admin.core.common.base.DtoConvert;
import com.kenfei.admin.modules.user.entity.SysDictDetailEntity;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * 数据字典详情(SysDictDetail)表单类
 *
 * @author kenfei
 * @since 2021-08-16 22:18:41
 */
@Data
public class SysDictDetailDto implements DtoConvert<SysDictDetailEntity> {

  /** 字典id */
  @NotNull private Long dictId;

  /** 字典标签 */
  @NotBlank private String label;

  /** 字典值 */
  @NotBlank private String value;

  /** 排序 */
  private Integer dictSort;
}
